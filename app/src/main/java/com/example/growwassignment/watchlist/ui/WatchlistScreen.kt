package com.example.growwassignment.watchlist.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.growwassignment.R
import com.example.growwassignment.gainerloser.ui.BottomNavigationBar
import com.example.growwassignment.appnavigation.mainactivitynavigation.Screen
import com.example.growwassignment.gainerloser.uistate.ShowLoadingState
import com.example.growwassignment.watchlist.roomentity.Watchlist
import com.example.growwassignment.watchlist.roomviewmodel.WatchlistViewModel
import com.example.growwassignment.watchlist.uistate.WatchlistUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(watchlistViewModel: WatchlistViewModel, navController: NavHostController) {


//    val userWatchlist = watchlistViewModel.allWatchlist.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        watchlistViewModel.fetchAllCreatedWatchlist()
    }

    val snackbarHostState = remember { SnackbarHostState() }

    val allWatchListUiState by watchlistViewModel.allWatchlist.collectAsState()


    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
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

        LaunchedEffect(allWatchListUiState) {
            if(allWatchListUiState is WatchlistUiState.Error){
                snackbarHostState.showSnackbar(
                    message = (allWatchListUiState as WatchlistUiState.Error).errorMessage,
                    actionLabel = "Ok"
                )
            }
        }
        when(val uiState = allWatchListUiState){
            is WatchlistUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(uiState.data) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            SingleWatchlist(it , onWatchlistClick = {
                                navController.navigate(Screen.WatchlistItem.createRoute(it.id , it.name)){// name and id of watchlist
                                    launchSingleTop = true
                                }
                            })
                        }
                    }
                }
            }
            is WatchlistUiState.Loading -> {
                ShowLoadingState()
            }
            is WatchlistUiState.Empty -> {
                Box(modifier = Modifier.fillMaxSize()){
                    Text(
                        "No watchlists found",
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> Unit
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

