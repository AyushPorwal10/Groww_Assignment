package com.example.growwassignment.gainerloser.marketdatamodels

import com.google.gson.annotations.SerializedName

data class CompanyOverviewData(
    val Symbol: String?,
    val AssetType: String?,
    val Name: String?,
    val Description: String?,
    val Exchange: String?,
    val Currency: String?,
    val Country: String?,
    val Sector: String?,
    val Industry: String?,
    val MarketCapitalization: String?,
    val PERatio: String?,
    val DividendYield: String?,
    val ProfitMargin: String?,
    val Beta : String?,
    @SerializedName("52WeekHigh")
    val weekHigh52: String?,
    @SerializedName("52WeekLow")
    val weekLow52: String?,
    )

