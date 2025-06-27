package com.example.growwassignment.gainerloser.marketdata

import com.google.gson.annotations.SerializedName

data class IntradayResponse(
    @SerializedName("Time Series (5min)")
    val timeSeries: Map<String, TimePoint>
)

data class TimePoint(
    @SerializedName("1. open") val open: String,
    @SerializedName("2. high") val high: String,
    @SerializedName("3. low") val low: String,
    @SerializedName("4. close") val close: String,
    @SerializedName("5. volume") val volume: String
)
