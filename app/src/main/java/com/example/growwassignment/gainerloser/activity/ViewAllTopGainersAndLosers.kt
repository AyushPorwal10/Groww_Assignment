package com.example.growwassignment.gainerloser.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.growwassignment.gainerloser.marketviewmodels.TopGainerLosersViewModel
import com.example.growwassignment.gainerloser.ui.CompanyOverviewScreen
import com.example.growwassignment.gainerloser.ui.TopGainersScreen
import com.example.growwassignment.gainerloser.ui.TopLosersScreen
import com.example.growwassignment.helper.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllTopGainersAndLosers : ComponentActivity() {
    private val topGainerLosersViewModel : TopGainerLosersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val gainerOrLosers = intent.getStringExtra("gainers_losers")
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController , startDestination = gainerOrLosers!!){
                composable(Screen.TopGainers.route){
                    TopGainersScreen(topGainerLosersViewModel ){ companyTicker->
                        navController.navigate(Screen.StockDetails.createRoute(companyTicker))
                    }
                }
                composable(Screen.TopLosers.route) {
                    TopLosersScreen(topGainerLosersViewModel)
                }
                composable(Screen.StockDetails.route){navBackStackEntry ->
                    val companyTicker = navBackStackEntry.arguments?.getString("companyTicker")
                    CompanyOverviewScreen(companyTicker , topGainerLosersViewModel)
                }
            }
        }
    }
}


