package com.example.growwassignment.roomdb.roomdao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.growwassignment.roomdb.roomentity.Watchlist
import com.example.growwassignment.roomdb.roomentity.WatchlistItem
import kotlinx.coroutines.flow.Flow


@Dao
interface WatchlistDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlist(watchlist: Watchlist)

    @Query("Select * from watchlist")
    fun getAllWatchlists() : Flow<List<Watchlist>>

    @Query("Select Exists(Select 1 from watchlist_item where ticker = :companySymbol Limit 1)")
    fun isCompanyStockSaved(companySymbol : String) : Flow<Boolean>



    @Query("Delete from watchlist_item where ticker = :companySymbol")
    suspend fun unsaveWatchlistItem(companySymbol: String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item  : WatchlistItem)

    @Query("Select * from watchlist_item where watchlistId = :id")
    fun getItemsByWatchlistId(id : Int) : Flow<List<WatchlistItem>>

    @Delete
    suspend fun deleteItem(item : WatchlistItem)
}