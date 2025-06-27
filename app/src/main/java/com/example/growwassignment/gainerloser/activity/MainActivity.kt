package com.example.growwassignment.gainerloser.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.growwassignment.gainerloser.marketviewmodels.MarketViewModel
import com.example.growwassignment.gainerloser.ui.HomeScreen
import com.example.growwassignment.helper.HomeRoutes
import com.example.growwassignment.ui.theme.GrowwAssignmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val marketViewModel : MarketViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GrowwAssignmentTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = HomeRoutes.Home.route){
                    composable(HomeRoutes.Home.route) {
                        HomeScreen(marketViewModel , navController)
                    }
                    composable(HomeRoutes.WatchList.route) {
                        WatchList()
                    }
                }
            }
        }
    }
}

@Composable
fun WatchList() {
    Text("Here user can see watch lists")
}


