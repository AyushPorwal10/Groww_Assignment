package com.example.growwassignment.watchlist.roomviewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.growwassignment.watchlist.roomentity.Watchlist
import com.example.growwassignment.watchlist.roomentity.WatchlistItem
import com.example.growwassignment.watchlist.uistate.WatchlistUiState
import com.example.growwassignment.watchlist.roomrepository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {


    // this is specific for watchlist bottom sheet
    val showAllWatchlist: Flow<List<Watchlist>> = watchlistRepository.getAllWatchlists()


    val LOG_TAG = "WATCHLIST"

    // Track UI state for watchlist (Loading , Error , Success)
    private val _watchlistUiState =
        MutableStateFlow<WatchlistUiState<List<WatchlistItem>>>(WatchlistUiState.Idle)
    val watchlistUiState: StateFlow<WatchlistUiState<List<WatchlistItem>>> = _watchlistUiState

    private val _allWatchlist =
        MutableStateFlow<WatchlistUiState<List<Watchlist>>>(WatchlistUiState.Idle)
    val allWatchlist: StateFlow<WatchlistUiState<List<Watchlist>>> = _allWatchlist.asStateFlow()

    // Fetch all created watchlist  by user
    fun fetchAllCreatedWatchlist() {
        viewModelScope.launch {
            _allWatchlist.value = WatchlistUiState.Loading

            watchlistRepository.getAllWatchlists()
                .catch {
                    _allWatchlist.value =
                        WatchlistUiState.Error("Something went wrong\nPlease try after some time")
                }.collect { watchlists ->

                    _allWatchlist.value =
                        if (watchlists.isEmpty()) WatchlistUiState.Empty else WatchlistUiState.Success(
                            watchlists
                        )

                }
        }
    }

    // checking if stock is already saved or not to update saved or unsaved status
    fun isCompanyStockSaved(symbol: String): Flow<Boolean> {
        return watchlistRepository.isCompanyStockSaved(symbol)
    }


    // Used to load items for a specific watchlist
    fun loadItemsFromWatchlist(watchlistId: Int) {
        viewModelScope.launch {
            Log.d(LOG_TAG, "Watchlist viewmodel , going to load items for watchlist id $watchlistId")
            _watchlistUiState.value = WatchlistUiState.Loading
            try {
                watchlistRepository.getItemsByWatchlistId(watchlistId)
                    .collect { listOfWatchlistItems ->
                        _watchlistUiState.value =
                            if (listOfWatchlistItems.isEmpty()) WatchlistUiState.Empty else  WatchlistUiState.Success(
                                listOfWatchlistItems
                            )
                    }
            } catch (e: Exception) {
                Log.d(LOG_TAG, "Watchlist viewmodel ,  Something went wrong in loading items , ${e.localizedMessage}")

                _watchlistUiState.value =
                    WatchlistUiState.Error("Failed to load items: ${e.localizedMessage}")
            }
        }
    }


    // if already saved and user click on icon , unsave it
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

    fun clearUiState() {
        _watchlistUiState.value = WatchlistUiState.Idle
    }

    // Create a new watchlist
    fun addWatchlist(name: String) {
        viewModelScope.launch {
            _watchlistUiState.value = WatchlistUiState.Loading
            try {
                Log.d(LOG_TAG, "Creating watchlist: $name")
                watchlistRepository.insertWatchlist(name)
                _watchlistUiState.value =
                    WatchlistUiState.SuccessMessage("Watchlist '$name' created")
            } catch (e: Exception) {
                _watchlistUiState.value =
                    WatchlistUiState.Error("Failed to create watchlist: ${e.localizedMessage}")
            }
        }
    }


    // Add a item to selected watchlist (using watchlistId)
    fun addItemToWatchlist(ticker: String, price: Double, watchlistId: Int) {
        viewModelScope.launch {
            _watchlistUiState.value = WatchlistUiState.Loading
            try {
                Log.d(LOG_TAG, "Adding item to watchlist: $ticker, $price, $watchlistId")
                watchlistRepository.insertItem(ticker, price, watchlistId)
                _watchlistUiState.value =
                    WatchlistUiState.SuccessMessage("Added $ticker to watchlist")
            } catch (e: Exception) {
                Log.d(LOG_TAG , "Something is wrong in adding watchlist item ")
                _watchlistUiState.value =
                    WatchlistUiState.Error("Failed to add item: ${e.localizedMessage}")
            }
        }
    }

}