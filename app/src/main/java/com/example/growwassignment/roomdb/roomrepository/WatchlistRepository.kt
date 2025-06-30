package com.example.growwassignment.roomdb.roomrepository

import com.example.growwassignment.roomdb.roomentity.Watchlist
import com.example.growwassignment.roomdb.roomdao.WatchlistDao
import com.example.growwassignment.roomdb.roomentity.WatchlistItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WatchlistRepository @Inject constructor(
    private val watchlistDao: WatchlistDao
) {

    fun getAllWatchlists(): Flow<List<Watchlist>> = watchlistDao.getAllWatchlists()

    fun getItemsByWatchlistId(id: Int): Flow<List<WatchlistItem>> =
        watchlistDao.getItemsByWatchlistId(id)

    suspend fun insertWatchlist(name : String ){
        val watchlist = Watchlist(name = name)
        watchlistDao.insertWatchlist(watchlist)
    }



    suspend fun insertItem(ticker : String , price : Double , watchlistId : Int){
        val watchlistItem = WatchlistItem(ticker = ticker , price = price , watchlistId = watchlistId)
        watchlistDao.insertItem(watchlistItem)
    }
    suspend fun deleteItem(watchlistItem : WatchlistItem){
        watchlistDao.deleteItem(watchlistItem)
    }

     fun isCompanyStockSaved(companySymbol : String ) : Flow<Boolean>{

        return watchlistDao.isCompanyStockSaved(companySymbol)
    }

    suspend fun unsaveWatchlistItem(companySymbol: String){
        watchlistDao.unsaveWatchlistItem(companySymbol)
    }



}
