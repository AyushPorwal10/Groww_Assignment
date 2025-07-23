package com.example.growwassignment.gainerloser.marketviewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.growwassignment.gainerloser.marketdatamodels.CompanyOverviewData
import com.example.growwassignment.gainerloser.marketdatamodels.StockItem
import com.example.growwassignment.gainerloser.marketrepo.MarketRepository
import com.example.growwassignment.gainerloser.checkdata.CheckCompanyOverviewData
import com.example.growwassignment.gainerloser.uistate.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TopGainerLosersViewModel @Inject constructor(
    private val marketRepository: MarketRepository
) : ViewModel() {


    // Tracking time of last api call made
    private var lastFetchedTime: Long = 0L

    /* Duration for which cache data will be valid */
    private val apiResponseCacheDuration = 5 * 60 * 1000L


    /* Specific to top gainers ui state management */
    private val _topMarketGainerUiState =
        MutableStateFlow<UiState<List<StockItem>>>(UiState.Loading)
    val topMarketGainersUiState: StateFlow<UiState<List<StockItem>>> = _topMarketGainerUiState

    /* Specific to top losers ui state management  */
    private val _topMarketLosersUiState =
        MutableStateFlow<UiState<List<StockItem>>>(UiState.Loading)
    val topMarketLosersUiState: StateFlow<UiState<List<StockItem>>> = _topMarketLosersUiState

    /* Specific to company overview ui state management  */
    private val _companyOverviewUiState =
        MutableStateFlow<UiState<CompanyOverviewData>>(UiState.Loading)
    val companyOverviewUiState: StateFlow<UiState<CompanyOverviewData>> = _companyOverviewUiState


    var priceData by mutableStateOf<List<Pair<String, Float>>>(emptyList())
        private set

    private val LOG_TAG = "TOP_GAINERS_LOSERS_VIEWMODEL"

    /* Checking if cache is valid (if api call was between last 5 min than cache is valid) */

    private fun isCacheValid(): Boolean {
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastFetchedTime) < apiResponseCacheDuration
    }


    /* used this fun to cache data for 5min and for case > 5min it will make a api call */

    fun loadTopMarket() {
        if (isCacheValid() && _topMarketGainerUiState.value is UiState.Success && _topMarketLosersUiState.value is UiState.Success) {

            Log.d(LOG_TAG, "No Api call,  using cached response")
            return
        }
        viewModelScope.launch {
            try {

                _topMarketGainerUiState.value = UiState.Loading
                _topMarketLosersUiState.value = UiState.Loading


                Log.d(LOG_TAG, "Making api call in $LOG_TAG")
                val response =
                    withContext(Dispatchers.IO) { marketRepository.fetchTopGainersLosers() }
                lastFetchedTime = System.currentTimeMillis()

                _topMarketGainerUiState.value =
                    if (response.top_gainers.isNullOrEmpty()) UiState.Empty
                    else UiState.Success(response.top_gainers)

                _topMarketLosersUiState.value =
                    if (response.top_losers.isNullOrEmpty()) UiState.Empty
                    else UiState.Success(response.top_losers)

            } catch (exception: Exception) {
                _topMarketGainerUiState.value =
                    UiState.Error(exception.localizedMessage ?: "Unknown Error")
                _topMarketLosersUiState.value =
                    UiState.Error(exception.localizedMessage ?: "Unknown Error")
                Log.d(LOG_TAG, "Something went wrong in TopGainersViewModel")
                exception.printStackTrace()
            }
        }
    }


    /* Using ticker (company symbol) to make api call , will return company details) */
    fun fetchCompanyOverview(companySymbol: String) {
        viewModelScope.launch {
            _companyOverviewUiState.value = UiState.Loading

            try {
               val response = withContext(Dispatchers.IO) {
                marketRepository.fetchCompanyOverview(companySymbol)
            }
                if (response.isSuccessful) {
                    Log.d(LOG_TAG, "Response is success")

                    val data = response.body()

                    if (data != null && !CheckCompanyOverviewData.isCompanyOverviewEmpty(data)) {
                        _companyOverviewUiState.value = UiState.Success(data)
                    } else {
                        Log.d(LOG_TAG, "Response body is null or empty")
                        _companyOverviewUiState.value = UiState.Empty
                    }
                } else {
                    Log.d(LOG_TAG, "Something went wrong ")
                    _companyOverviewUiState.value = UiState.Error("No data Available")
                }
            } catch (e: Exception) {
                _companyOverviewUiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }

        }
    }


    /* fetching data points to be plot on line graph . (Fetching with full parameter to get enough data points instead of 100 data points) */
    fun loadPricesGraph(companySymbol: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
            marketRepository.getIntradayPrices(companySymbol)
        }
            result.onSuccess { rawList ->
                val sortedList = rawList.sortedBy {
                    it.first
                }
                priceData = sortedList
            }.onFailure {
                Log.d(LOG_TAG, "Something went wrong while fetching Prices ${it.message}")
            }
        }
    }
}
