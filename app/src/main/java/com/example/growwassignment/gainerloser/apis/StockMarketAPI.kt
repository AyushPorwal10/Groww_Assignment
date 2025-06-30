package com.example.growwassignment.gainerloser.apis

import com.example.growwassignment.gainerloser.marketdatamodels.CompanyOverviewData
import com.example.growwassignment.gainerloser.marketdatamodels.IntradayResponse
import com.example.growwassignment.gainerloser.marketdatamodels.TopGainersLosers
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StockMarketAPI {

    @GET("query")
    suspend fun getTopGainersLosers(
        @Query("function") function: String = "TOP_GAINERS_LOSERS",
    ) : TopGainersLosers


    @GET("query")
    suspend fun getCompanyOverview(
        @Query("function") function : String = "OVERVIEW",
        @Query("symbol")symbol : String
    ) : Response<CompanyOverviewData>


   @GET("query")
   suspend fun getIntradayData(
       @Query("function") function : String = "TIME_SERIES_INTRADAY",
       @Query("symbol")symbol: String,
       @Query("interval")interval : String = "5min",
       @Query("outputsize")outputSize : String = "full"   //here i am fetching all because graph points can be shown in perfect manner
   ) : Response<IntradayResponse>

}