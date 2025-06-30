package com.example.growwassignment.watchlist.uistate

sealed class WatchlistUiState<out T>{
    object Idle : WatchlistUiState<Nothing>()
    object Loading : WatchlistUiState<Nothing>()
    data class Success<T>(val data : T) : WatchlistUiState<T>()
    data class Error(val errorMessage : String) : WatchlistUiState<Nothing>()
    data class SuccessMessage(val successMessage : String) : WatchlistUiState<Nothing>()
    object Empty : WatchlistUiState<Nothing>()
}