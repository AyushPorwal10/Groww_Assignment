package com.example.growwassignment.helper

sealed class Screen(val route: String) {
    object TopGainers : Screen("topGainers")
    object TopLosers : Screen("topLosers")
    object StockDetails : Screen("stockDetails/{companyTicker}"){
        fun createRoute(companyTicker : String) = "stockDetails/$companyTicker"
    }
}
sealed class HomeRoutes(val route : String){
    object Home : HomeRoutes("home")
    object WatchList : HomeRoutes("watchList")
}