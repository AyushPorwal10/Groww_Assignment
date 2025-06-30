package com.example.growwassignment.gainerloser.marketdatamodels


data class TopGainersLosers(
    val top_gainers: List<StockItem>?,
    val top_losers: List<StockItem>?,
    val most_actively_traded: List<StockItem>?
)

data class StockItem(
    val ticker: String,      // company symbol
    val price: String,
    val change_percentage : String,
)