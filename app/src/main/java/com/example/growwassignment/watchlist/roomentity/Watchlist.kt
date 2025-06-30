package com.example.growwassignment.watchlist.roomentity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class Watchlist(
    @PrimaryKey(autoGenerate = true)val id : Int = 0,
    val name : String
)

@Entity(
    tableName = "watchlist_item",
    foreignKeys = [
        ForeignKey(
            entity = Watchlist::class,
            parentColumns = ["id"],
            childColumns = ["watchlistId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["watchlistId", "ticker"], unique = true)]
)
data class WatchlistItem(
    @PrimaryKey(autoGenerate = true)val id : Int = 0,
    val ticker : String,
    val price : Double ,
    val watchlistId: Int
)