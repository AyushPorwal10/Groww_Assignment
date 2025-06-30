package com.example.growwassignment.gainerloser.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import com.example.growwassignment.watchlist.ui.ShowWatchlistsItems
import com.example.growwassignment.gainerloser.ui.TopGainersScreen
import com.example.growwassignment.gainerloser.ui.TopLosersScreen
import com.example.growwassignment.watchlist.ui.WatchlistScreen
import com.example.growwassignment.appnavigation.mainactivitynavigation.Screen
import com.example.growwassignment.watchlist.roomviewmodel.WatchlistViewModel
import com.example.growwassignment.ui.theme.GrowwAssignmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    private val marketViewModel : MarketViewModel by viewModels()
    private val topGainerLosersViewModel : TopGainerLosersViewModel by viewModels()
    private val watchlistViewModel : WatchlistViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GrowwAssignmentTheme {

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.Home.route){


                    composable(Screen.Home.route) {

                        // Home screen is to show top gainer and top losers
                        HomeScreen(marketViewModel , navController)
                    }
                    composable(Screen.Watchlist.route) {
                        // this will show the list watchlist , user can click on specific watchlist and can browse items in that watchlist
                        WatchlistScreen(watchlistViewModel , navController)
                    }
                    composable(Screen.SearchTicker.route){

                        // this screen will help user to search stocks items based on stock symbol
                        SearchTickerScreen(marketViewModel , navController)
                    }

                    composable(
                        Screen.WatchlistItem.route,
                        arguments = listOf(
                            navArgument("watchlistId"){type = NavType.StringType},
                            navArgument("name"){type = NavType.StringType}
                        )
                    ) { navBackStackEntry ->

                        // watchlist id is used to fetch watch list items from room database that user saved

                        val watchlistId = navBackStackEntry.arguments?.getString("watchlistId")?.toInt()
                        // this shows the name of watchlist
                        val watchlistName = navBackStackEntry.arguments?.getString("name")
                        ShowWatchlistsItems(watchlistViewModel , navController , watchlistId , watchlistName)
                    }
                    composable(Screen.TopGainers.route){
                        TopGainersScreen(topGainerLosersViewModel , navController)
                    }
                    composable(Screen.TopLosers.route) {

                        // This screen shows top 20 gainers
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

                        // Price is used to show the current price of stock for a company
                        // Price is field that is not in company overview Json , so i passed it
                        // Ticker is used to make api call (using it as a parameter to fetch company details)
                        CompanyOverviewScreen(companyTicker ,price, topGainerLosersViewModel , watchlistViewModel)
                    }
                }
            }
        }
    }
}


