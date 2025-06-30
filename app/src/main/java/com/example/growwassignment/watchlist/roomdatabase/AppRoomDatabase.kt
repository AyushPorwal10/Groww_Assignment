package com.example.growwassignment.watchlist.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.growwassignment.watchlist.roomdao.WatchlistDao
import com.example.growwassignment.watchlist.roomentity.Watchlist
import com.example.growwassignment.watchlist.roomentity.WatchlistItem

@Database(entities = [Watchlist::class , WatchlistItem::class] , version = 1)
abstract class AppRoomDatabase : RoomDatabase(){
    abstract fun watchlistDao() : WatchlistDao
}