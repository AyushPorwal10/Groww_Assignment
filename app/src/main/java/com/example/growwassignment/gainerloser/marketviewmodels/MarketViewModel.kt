package com.example.growwassignment.gainerloser.marketviewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.growwassignment.gainerloser.marketdatamodels.StockItem
import com.example.growwassignment.gainerloser.marketdatamodels.StockSearchResponse
import com.example.growwassignment.gainerloser.marketdatamodels.TopGainersLosers
import com.example.growwassignment.gainerloser.marketrepo.MarketRepository
import com.example.growwassignment.gainerloser.uistate.StockSearchUiState
import com.example.growwassignment.gainerloser.uistate.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MarketViewModel @Inject constructor(
    // injecting repository using using Hilt's constructor injection
    private val marketRepository: MarketRepository
) : ViewModel(){


    /* This is used to update ui (Loading State, Error State, Empty State ) */

    private val _uiState = MutableStateFlow<UiState<TopGainersLosers>>(UiState.Loading)
    val uiState : StateFlow<UiState<TopGainersLosers>>get() = _uiState.asStateFlow()


    /* Used this to make log statement and find why something not works */
   private val LOG_TAG = "TOPGAINERSLOSERSVIEWMODEL"



    /* This is used to keep track of last api call , if last api call was older than 5 min , new call can be done  */
    private var lastFetchTime : Long = 0L
    // caching 5 min
    private val apiResponseCacheDuration = 5*60 * 1000L

    private var fullMarketData: TopGainersLosers? = null



    /* Used to Holds the raw user input for search queries   */
    private val _searchQueryUiState = MutableStateFlow("")

    /* This will expose the current state of the search operation (Idle, Loading, Success, Error, Empty)  */
    val searchQueryUiState = MutableStateFlow<StockSearchUiState>(StockSearchUiState.Idle)

    /* Checks if the cached API response is still valid based on a predefined duration */
    private fun isCacheValid(): Boolean {
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastFetchTime) < apiResponseCacheDuration
    }

    init {
        loadTopMarket()
        initializeSearch()
    }

    /* initializes the search handling logic with debounce and filtering to  avoid searching again and again */
    private fun initializeSearch() {
        viewModelScope.launch {
            _searchQueryUiState
                .debounce(400L)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .collectLatest { query ->
                    performSearch(query)
                }
        }
    }

    /* Updates the search query input state from the Ui */
    fun onSearchQueryChanged(query: String) {
        _searchQueryUiState.value = query
    }

    /* Performs  search using the repository and updates the UI state based on the result: loading, success, error, or empty */
    private suspend fun performSearch(query: String) {
        if (query.isEmpty()) {
            searchQueryUiState.value = StockSearchUiState.Empty
            return
        }

        searchQueryUiState.value = StockSearchUiState.Loading
        try {
            val result = marketRepository.getStocksItemsByKeyword(query)
            searchQueryUiState.value = StockSearchUiState.Success(result.bestMatches)
        } catch (e: Exception) {
            searchQueryUiState.value = StockSearchUiState.Error(e.localizedMessage ?: "Unknown error")
        }
    }




    fun loadTopMarket(){

        /* This check will make sure that if cache is valid and state is success that cached data can be used */

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
