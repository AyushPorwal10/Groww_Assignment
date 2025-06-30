package com.example.growwassignment.gainerloser.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Watchlist::class , WatchlistItem::class] , version = 1)
abstract class AppRoomDatabase : RoomDatabase(){
    abstract fun watchlistDao() : WatchlistDao
}