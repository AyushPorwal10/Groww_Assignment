package com.example.growwassignment.appnavigation.mainactivitynavigation

sealed class Screen(val route: String) {
    object TopGainers : Screen("topGainers")
    object TopLosers : Screen("topLosers")
    object StockDetails : Screen("stockDetails/{companyTicker}/{price}"){
        fun createRoute(companyTicker : String , price : Double) = "stockDetails/$companyTicker/$price"
    }
    object Home : Screen("home")
    object Watchlist : Screen("watchList")
    object WatchlistItem : Screen("watchlistItems/{watchlistId}/{name}"){   // watchlist id to fetch watchlist items and name to show name of watchlist
        fun createRoute(watchlistId : Int, name : String ) = "watchlistItems/$watchlistId/$name"
    }

    object SearchTicker : Screen("search")

}
