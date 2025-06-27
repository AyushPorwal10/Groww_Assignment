package com.example.growwassignment.gainerloser.ui

import android.content.Intent
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.growwassignment.gainerloser.activity.ViewAllTopGainersAndLosers
import com.example.growwassignment.gainerloser.marketdata.StockItem
import com.example.growwassignment.gainerloser.marketviewmodels.MarketViewModel
import com.example.growwassignment.helper.HomeRoutes
import com.example.growwassignment.helper.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(marketViewModel: MarketViewModel, navController: NavHostController) {

    val topGainersLosersInitialData =
        marketViewModel.topMarketGainersLosersData.collectAsState()

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.stocks_app))
            })
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Section(title = stringResource(R.string.top_gainers)) {
                    val navigateToShowAllTopGainer = Intent(context , ViewAllTopGainersAndLosers::class.java)
                    // this information is used to make decisions which stock to show either losers or gainers

                    navigateToShowAllTopGainer.putExtra("gainers_losers", Screen.TopGainers.route)
                    context.startActivity(navigateToShowAllTopGainer)
                }
            }

            item {
                StockGrid(topGainersLosersInitialData.value?.top_gainers)
            }

            item {
                Section(title = stringResource(R.string.top_losers)) {
                    val navigateToShowAllTopGainer = Intent(context , ViewAllTopGainersAndLosers::class.java)
                    // this information is used to make decisions which stock to show either losers or gainers

                    navigateToShowAllTopGainer.putExtra("gainers_losers",Screen.TopLosers.route)

                    context.startActivity(navigateToShowAllTopGainer)
                }
            }

            item {
                StockGrid(topGainersLosersInitialData.value?.top_losers)
            }
        }
    }
}


@Composable
fun StockGrid(gainers: List<StockItem>?) {

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
            StockItemBox(stock)
        }
    }
}


@Composable
fun StockItemBox(stock: StockItem) {
    Card(
        modifier = Modifier
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(),
        onClick = {

        }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {

                // if change Percent is < 0 that mean losers are there
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
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = {
                navController.navigate(HomeRoutes.Home.route){
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
            selected = true,
            onClick = {
                navController.navigate(HomeRoutes.WatchList.route){
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
