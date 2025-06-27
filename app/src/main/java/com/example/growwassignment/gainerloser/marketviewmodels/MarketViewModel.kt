package com.example.growwassignment.gainerloser.marketviewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.growwassignment.gainerloser.marketdata.TopGainersLosers
import com.example.growwassignment.gainerloser.marketrepo.MarketRepository
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


    private val _topMarketGainersLosersData = MutableStateFlow<TopGainersLosers?>(null)
    val topMarketGainersLosersData : StateFlow<TopGainersLosers?>get() = _topMarketGainersLosersData.asStateFlow()


   private val LOG_TAG = "TOPGAINERSLOSERSVIEWMODEL"

    init {
        viewModelScope.launch {
            try{
                val response = marketRepository.fetchTopGainersLosers()
                _topMarketGainersLosersData.value = response
                Log.d(LOG_TAG , "Size is " + topMarketGainersLosersData.value?.top_gainers?.size)
            }
            catch (exception : Exception){
                Log.d(LOG_TAG , "Something went wrong in TopGainersLosersViewModel")
                exception.printStackTrace()
            }
        }
    }

}