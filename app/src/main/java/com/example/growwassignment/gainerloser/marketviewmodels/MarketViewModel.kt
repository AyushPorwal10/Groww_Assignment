package com.example.growwassignment.gainerloser.marketviewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.growwassignment.gainerloser.marketdatamodels.StockItem
import com.example.growwassignment.gainerloser.marketdatamodels.TopGainersLosers
import com.example.growwassignment.gainerloser.marketrepo.MarketRepository
import com.example.growwassignment.gainerloser.uistate.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MarketViewModel @Inject constructor(
    private val marketRepository: MarketRepository
) : ViewModel(){



    private val _uiState = MutableStateFlow<UiState<TopGainersLosers>>(UiState.Loading)
    val uiState : StateFlow<UiState<TopGainersLosers>>get() = _uiState.asStateFlow()

   private val LOG_TAG = "TOPGAINERSLOSERSVIEWMODEL"

    private var lastFetchTime : Long = 0L
    // caching 5 min
    private val apiResponseCacheDuration = 5*50 * 1000L

    private var fullMarketData: TopGainersLosers? = null
    private val _searchResults = MutableStateFlow<List<StockItem>>(emptyList())
    val searchResults : StateFlow<List<StockItem>> = _searchResults.asStateFlow()

    private fun isCacheValid():Boolean {
         val currentTime = System.currentTimeMillis()
         return (currentTime - lastFetchTime) < apiResponseCacheDuration
    }

    init {
        loadTopMarket()
    }

    //ticker is company symbol
    fun searchByTicker(companyTicker : String){
        viewModelScope.launch {
            val query = companyTicker.trim().lowercase()

            if (query.isEmpty()) {
                _searchResults.value = emptyList()
                return@launch
            }

            val results = fullMarketData?.let { data->
                val combined = mutableListOf<StockItem>()
                data.top_gainers?.let { combined += it }
                data.top_losers?.let { combined += it }
                data.most_actively_traded?.let { combined += it }

                combined.filter { it.ticker.lowercase().contains(query) }
            }
            _searchResults.value = results ?: emptyList()
        }
    }
    fun loadTopMarket(){
        if(isCacheValid() && _uiState.value is UiState.Success){
            Log.d(LOG_TAG , "No Api call using cached response")
            return
        }


        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try{
                Log.d(LOG_TAG , "Making api call in $LOG_TAG")
                val response = marketRepository.fetchTopGainersLosers()

                if (response.top_gainers.isNullOrEmpty() && response.top_losers.isNullOrEmpty()) {
                    _uiState.value = UiState.Empty
                } else {
                    fullMarketData = response
                    _uiState.value = UiState.Success(response)
                    lastFetchTime = System.currentTimeMillis()
                }

            }
            catch (exception : Exception){
                _uiState.value = UiState.Error("Something went wrong: ${exception.localizedMessage}")
                Log.d(LOG_TAG , "Something went wrong in TopGainersLosersViewModel")
                exception.printStackTrace()
            }
        }

    }

}