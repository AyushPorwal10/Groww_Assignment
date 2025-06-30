package com.example.growwassignment.gainerloser.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.growwassignment.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.growwassignment.gainerloser.marketdatamodels.StockItem
import com.example.growwassignment.gainerloser.marketviewmodels.MarketViewModel
import com.example.growwassignment.appnavigation.mainactivitynavigation.Screen
import com.example.growwassignment.gainerloser.uistate.ShowErrorState
import com.example.growwassignment.gainerloser.uistate.ShowLoadingState
import com.example.growwassignment.gainerloser.uistate.ShowUserThatNoDataAvailable
import com.example.growwassignment.gainerloser.uistate.UiState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(marketViewModel: MarketViewModel, navController: NavHostController) {


    val uiState by marketViewModel.uiState.collectAsState()


    var isRefreshing by remember { mutableStateOf(false) }

    val refreshScope = rememberCoroutineScope()

    fun refresh(){
        refreshScope.launch {
            isRefreshing = true
            marketViewModel.loadTopMarket()
            isRefreshing = false
        }
    }

    Scaffold(
        topBar = {
            Column {
                    TopAppBar(title = {
                        Text(text = stringResource(R.string.stocks_app))
                    },
                        actions = {
                            IconButton(onClick = {
                                navController.navigate(Screen.SearchTicker.route)
                            }) {
                                Icon(imageVector = Icons.Default.Search , contentDescription = null)
                            }
                        })
                HorizontalDivider(thickness = 1.dp)
            }

        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->

        SwipeRefresh(
            state = SwipeRefreshState(isRefreshing),
            onRefresh = {refresh()},
            modifier = Modifier.padding(paddingValues)
        ) {

            when(uiState){
                is UiState.Loading -> {
                   ShowLoadingState()
                }
                is UiState.Error -> {
                    val errorMessage = (uiState as UiState.Error).errorMessage
                    ShowErrorState(errorMessage)
                }

                is UiState.Empty -> {
                   ShowUserThatNoDataAvailable()
                }
                is UiState.Success -> {
                    val data = (uiState as UiState.Success).data
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Section(title = stringResource(R.string.top_gainers)) {
                                navController.navigate(Screen.TopGainers.route)
                            }
                        }

                        item {
                            StockGrid(data.top_gainers, navController)
                        }

                        item {
                            Section(title = stringResource(R.string.top_losers)) {
                                navController.navigate(Screen.TopLosers.route)
                            }
                        }

                        item {
                            StockGrid(data.top_losers, navController)
                        }
                    }
                }
            }

        }

    }
}


@Composable
fun StockGrid(gainers: List<StockItem>? , navController: NavHostController) {

    val firstFourData = gainers?.take(4) ?: emptyList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(firstFourData.size) { index ->
            val stock = firstFourData[index]
            StockItemBox(stock , navController)
        }
    }
}


@Composable
fun StockItemBox(stock: StockItem, navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(),
        onClick = {
            Log.d("TickerAndPrice","Home Screen Ticker is ${stock.ticker} price is ${stock.price}")
            navController.navigate(Screen.StockDetails.createRoute(stock.ticker , stock.price.toDouble()))
        }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {

                // if change Percent is < 0 that mean losers else gainers
                val changePercent = stock.change_percentage.substringBefore("%").toDoubleOrNull() ?: 0.0
                val iconRes = if (changePercent < 0) R.drawable.top_losers else R.drawable.top_gainers
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = if(iconRes == R.drawable.top_gainers) Color.Green else Color.Red
                )
            }


            Spacer(modifier = Modifier.height(8.dp))

            Text(text = stock.ticker, style = MaterialTheme.typography.bodyMedium)
            Text(text = "$" + stock.price, style = MaterialTheme.typography.bodySmall)

        }
    }
}

@Composable
fun Section(title: String, onViewAllClick: () -> Unit) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        TextButton(onClick = {
            onViewAllClick()
        }) {
            Text(text = stringResource(R.string.view_all))
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    NavigationBar {
        NavigationBarItem(
            selected = currentDestination?.route == Screen.Home.route,
            onClick = {
                navController.navigate(Screen.Home.route){
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(imageVector = Icons.Default.Home, contentDescription = "${R.string.home}")
            },
            label = {
                Text(stringResource(R.string.home))
            }
        )
        NavigationBarItem(
            selected = currentDestination?.route == Screen.Watchlist.route,
            onClick = {
                navController.navigate(Screen.Watchlist.route){
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "${R.string.watch_list}"
                )
            },
            label = {
                Text(stringResource(R.string.watch_list))
            }
        )
    }
}
