package com.example.growwassignment.gainerloser.uistate

import com.example.growwassignment.gainerloser.marketdatamodels.StockSearchItem

sealed class StockSearchUiState {
    object Idle : StockSearchUiState()   // Nothing to be done
    object Loading : StockSearchUiState()   // Fetching data
    data class Success(val items: List<StockSearchItem>) : StockSearchUiState()  // Data fetched successfully
    data class Error(val message: String) : StockSearchUiState()  // error while fetching data
    object Empty : StockSearchUiState()  // fetched data is empty
}
