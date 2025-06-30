package com.example.growwassignment.gainerloser.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface WatchlistDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlist(watchlist: Watchlist)

    @androidx.room.Query("Select * from watchlist")
    fun getAllWatchlists() : Flow<List<Watchlist>>

    @Delete
    suspend fun deleteWatchlist(watchlist : Watchlist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item  :WatchlistItem)

    @Query("Select * from watchlist_item where watchlistId = :id")
    fun getItemsByWatchlistId(id : Int) : Flow<List<WatchlistItem>>

    @Delete
    suspend fun deleteItem(item : WatchlistItem)
}