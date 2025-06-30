package com.example.growwassignment.gainerloser.marketrepo

import com.example.growwassignment.gainerloser.marketdatamodels.CompanyOverviewData
import com.example.growwassignment.gainerloser.apis.StockMarketAPI
import com.example.growwassignment.gainerloser.marketdatamodels.StockSearchResponse
import retrofit2.Response
import javax.inject.Inject

class MarketRepository @Inject constructor(
    private val marketAPI: StockMarketAPI
) {
    suspend fun fetchTopGainersLosers() = marketAPI.getTopGainersLosers()

    suspend fun fetchCompanyOverview(companySymbol : String) : Response<CompanyOverviewData> {
        return marketAPI.getCompanyOverview(symbol = companySymbol)
    }

    suspend fun getIntradayPrices(companySymbol: String) :Result<List<Pair<String, Float>>>{
        return try{
            val pricesResponse = marketAPI.getIntradayData(symbol = companySymbol)
            if(pricesResponse.isSuccessful){
                val body = pricesResponse.body()
                val list = body?.timeSeries?.entries?.map {
                    it.key to it.value.close.toFloat()
                } ?: emptyList()
                Result.success(list)
            }
            else {
                Result.failure(Exception("Error ${pricesResponse.code()}"))
            }
        }
        catch (exception : Exception){
            Result.failure(exception)
        }
    }

    suspend fun getStocksItemsByKeyword(searchKeyword : String) : StockSearchResponse{
        return marketAPI.getStocksItemsByKeyword(keywords = searchKeyword)
    }


}