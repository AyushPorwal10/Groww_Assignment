package com.example.growwassignment.roomdb.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.growwassignment.R
import com.example.growwassignment.gainerloser.ui.BottomNavigationBar
import com.example.growwassignment.appnavigation.mainactivitynavigation.Screen
import com.example.growwassignment.roomdb.roomentity.Watchlist
import com.example.growwassignment.roomdb.roomviewmodel.WatchlistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(watchlistViewModel: WatchlistViewModel, navController: NavHostController) {


    val userWatchlist = watchlistViewModel.allWatchlist.collectAsState(emptyList())

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = {
                    Text("Watchlist", fontWeight = FontWeight.SemiBold)
                })
                HorizontalDivider(thickness = 2.dp)
            }
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->




                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(userWatchlist.value) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            SingleWatchlist(it , onWatchlistClick = {
                                navController.navigate(Screen.WatchlistItem.createRoute(it.id , it.name))
                            })
                        }
                    }
                }
    }
}

@Composable
fun SingleWatchlist(watchlist: Watchlist, onWatchlistClick : () -> Unit ) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp)
                .clickable {
                    onWatchlistClick()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(text = watchlist.name, style = MaterialTheme.typography.titleLarge)

            Icon(
                painter = painterResource(R.drawable.right_arrow),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .size(20.dp)
            )
        }
        HorizontalDivider(thickness = 2.dp, modifier = Modifier.fillMaxWidth(0.95f))
    }
}

