package com.example.growwassignment.gainerloser.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.growwassignment.gainerloser.marketviewmodels.MarketViewModel
import com.example.growwassignment.gainerloser.marketviewmodels.TopGainerLosersViewModel
import com.example.growwassignment.gainerloser.ui.CompanyOverviewScreen
import com.example.growwassignment.gainerloser.ui.HomeScreen
import com.example.growwassignment.gainerloser.ui.SearchTickerScreen
import com.example.growwassignment.roomdb.ui.ShowWatchlistsItems
import com.example.growwassignment.gainerloser.ui.TopGainersScreen
import com.example.growwassignment.gainerloser.ui.TopLosersScreen
import com.example.growwassignment.roomdb.ui.WatchlistScreen
import com.example.growwassignment.appnavigation.mainactivitynavigation.Screen
import com.example.growwassignment.roomdb.roomviewmodel.WatchlistViewModel
import com.example.growwassignment.ui.theme.GrowwAssignmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val marketViewModel : MarketViewModel by viewModels()
    private val topGainerLosersViewModel : TopGainerLosersViewModel by viewModels()
    private val watchlistViewModel : WatchlistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GrowwAssignmentTheme {

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.Home.route){

                    composable(Screen.Home.route) {
                        HomeScreen(marketViewModel , navController)
                    }
                    composable(Screen.Watchlist.route) {
                        WatchlistScreen(watchlistViewModel , navController)
                    }
                    composable(Screen.SearchTicker.route){
                        SearchTickerScreen(marketViewModel , navController)
                    }

                    composable(
                        Screen.WatchlistItem.route,
                        arguments = listOf(
                            navArgument("watchlistId"){type = NavType.StringType},
                            navArgument("name"){type = NavType.StringType}
                        )
                    ) { navBackStackEntry ->
                        val watchlistId = navBackStackEntry.arguments?.getString("watchlistId")?.toInt()
                        val watchlistName = navBackStackEntry.arguments?.getString("name")
                        ShowWatchlistsItems(watchlistViewModel , navController , watchlistId , watchlistName)
                    }
                    composable(Screen.TopGainers.route){
                        TopGainersScreen(topGainerLosersViewModel , navController)
                    }
                    composable(Screen.TopLosers.route) {
                        TopLosersScreen(topGainerLosersViewModel, navController)
                    }


                    composable(
                        Screen.StockDetails.route ,
                        arguments = listOf(
                            navArgument("companyTicker") {type = NavType.StringType},
                            navArgument("price"){type = NavType.FloatType}
                        )
                    ){ navBackStackEntry ->

                        val companyTicker = navBackStackEntry.arguments?.getString("companyTicker")
                        val price = navBackStackEntry.arguments?.getFloat("price")?.toDouble()

                        Log.d("TICKER", "TICKER is $companyTicker")
                        Log.d("TICKER", "Price is $price")
                        CompanyOverviewScreen(companyTicker ,price, topGainerLosersViewModel , watchlistViewModel)
                    }
                }
            }
        }
    }
}


