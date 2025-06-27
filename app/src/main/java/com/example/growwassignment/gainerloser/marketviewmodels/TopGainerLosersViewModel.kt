package com.example.growwassignment.gainerloser.marketviewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.growwassignment.gainerloser.marketdata.CompanyOverviewData
import com.example.growwassignment.gainerloser.marketdata.StockItem
import com.example.growwassignment.gainerloser.marketrepo.MarketRepository
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TopGainerLosersViewModel @Inject constructor(
    private val marketRepository: MarketRepository
) : ViewModel(){


    private val _topMarketGainersData = MutableStateFlow<List<StockItem>?>(null)
    val topMarketGainersData : StateFlow<List<StockItem>?>
        get() = _topMarketGainersData.asStateFlow()

    private val _topMarketLosersData = MutableStateFlow<List<StockItem>?>(null)
    val topMarketLosersData : StateFlow<List<StockItem>?>
        get() = _topMarketLosersData.asStateFlow()


    private val _companyOverviewData = MutableStateFlow<CompanyOverviewData?>(null)
    val companyOverviewData: StateFlow<CompanyOverviewData?> = _companyOverviewData.asStateFlow()


    var priceData by mutableStateOf<List<Pair<String, Float>>>(emptyList())
        private set


    private val LOG_TAG = "TOP_GAINERS_LOSERS_VIEWMODEL"

    init {
        viewModelScope.launch {
            try{
                val response = withContext(Dispatchers.IO){
                    marketRepository.fetchTopGainersLosers()
                }
                _topMarketGainersData.value = response.top_gainers
                _topMarketLosersData.value = response.top_losers
            }
            catch (exception : Exception){
                Log.d(LOG_TAG , "Something went wrong in TopGainersViewModel")
                exception.printStackTrace()
            }
        }
    }


    // make sure to remove call from activity that u wrote for debugging
    fun fetchCompanyOverview(companySymbol : String){
        viewModelScope.launch {

            try{
                val overviewResponse = withContext(Dispatchers.IO){
                    marketRepository.fetchCompanyOverview(companySymbol)
                }

                if(overviewResponse.isSuccessful){
                    _companyOverviewData.value = overviewResponse.body()
                    Log.d(LOG_TAG , "Data fetched : ${companyOverviewData.value}")
                }
                else {
                    Log.d(LOG_TAG , "Data fetched is null")
                    _companyOverviewData.value = null
                }
            }
            catch (exception : Exception){
                Log.d(LOG_TAG , "Something went wrong while fetching company overview \n Error is : $exception")
            }

        }
    }

    fun loadPricesGraph(companySymbol : String ){
        viewModelScope.launch {
            Log.d(LOG_TAG , "Loading prices for $companySymbol <-")
            val result = marketRepository.getIntradayPrices(companySymbol)
            result.onSuccess { rawList ->
                val sortedList = rawList.sortedBy { it.first }

                priceData = sortedList

                Log.d(LOG_TAG, "Sorted Price Data -> ")
                sortedList.forEach { (time, price) ->
                    Log.d(LOG_TAG, "$time -> $price")
                }

                Log.d(LOG_TAG, "Price data size is ${priceData.size}")
            }.onFailure {
                Log.d(LOG_TAG, "Something went wrong while fetching Prices ${it.message}")
            }
        }
    }



}