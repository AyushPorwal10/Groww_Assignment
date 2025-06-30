package com.example.growwassignment.roomdb.roomviewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.growwassignment.roomdb.roomentity.Watchlist
import com.example.growwassignment.roomdb.roomentity.WatchlistItem
import com.example.growwassignment.roomdb.uistate.WatchlistUiState
import com.example.growwassignment.roomdb.roomrepository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {


    val allWatchlist: Flow<List<Watchlist>> = watchlistRepository.getAllWatchlists()


    val LOG_TAG = "WATCHLIST"

    private val _watchlistUiState =
        MutableStateFlow<WatchlistUiState<List<WatchlistItem>>>(WatchlistUiState.Idle)
    val watchlistUiState: StateFlow<WatchlistUiState<List<WatchlistItem>>> = _watchlistUiState








    fun isCompanyStockSaved(symbol: String): Flow<Boolean> {
        return watchlistRepository.isCompanyStockSaved(symbol)
    }

    fun loadItemsFromWatchlist(watchlistId: Int) {
        viewModelScope.launch {
            _watchlistUiState.value = WatchlistUiState.Loading
            try {
                watchlistRepository.getItemsByWatchlistId(watchlistId).collect {
                    _watchlistUiState.value = WatchlistUiState.Success(it)
                }
            } catch (e: Exception) {
                _watchlistUiState.value =
                    WatchlistUiState.Error("Failed to load items: ${e.localizedMessage}")
            }
        }
    }

    fun unsaveWatchlistItem(companySymbol: String) {
        viewModelScope.launch {
            _watchlistUiState.value = WatchlistUiState.Loading
            try {
                watchlistRepository.unsaveWatchlistItem(companySymbol)
                _watchlistUiState.value = WatchlistUiState.SuccessMessage("Removed from watchlist")
            } catch (e: Exception) {
                _watchlistUiState.value =
                    WatchlistUiState.Error("Failed to remove: ${e.localizedMessage}")
            }
        }
    }

    fun addWatchlist(name: String) {
        viewModelScope.launch {
            _watchlistUiState.value = WatchlistUiState.Loading
            try {
                Log.d(LOG_TAG, "Creating watchlist: $name")
                watchlistRepository.insertWatchlist(name)
                _watchlistUiState.value = WatchlistUiState.SuccessMessage("Watchlist '$name' created")
            } catch (e: Exception) {
                _watchlistUiState.value =
                    WatchlistUiState.Error("Failed to create watchlist: ${e.localizedMessage}")
            }
        }
    }


    fun addItemToWatchlist(ticker: String, price: Double, watchlistId: Int) {
        viewModelScope.launch {
            _watchlistUiState.value = WatchlistUiState.Loading
            try {
                Log.d(LOG_TAG, "Adding item to watchlist: $ticker, $price, $watchlistId")
                watchlistRepository.insertItem(ticker, price, watchlistId)
                _watchlistUiState.value = WatchlistUiState.SuccessMessage("Added $ticker to watchlist")
            } catch (e: Exception) {
                _watchlistUiState.value = WatchlistUiState.Error("Failed to add item: ${e.localizedMessage}")
            }
        }
    }

}