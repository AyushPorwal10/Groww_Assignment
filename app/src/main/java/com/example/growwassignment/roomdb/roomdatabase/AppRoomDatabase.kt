package com.example.growwassignment.roomdb.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.growwassignment.roomdb.roomdao.WatchlistDao
import com.example.growwassignment.roomdb.roomentity.Watchlist
import com.example.growwassignment.roomdb.roomentity.WatchlistItem

@Database(entities = [Watchlist::class , WatchlistItem::class] , version = 1)
abstract class AppRoomDatabase : RoomDatabase(){
    abstract fun watchlistDao() : WatchlistDao
}