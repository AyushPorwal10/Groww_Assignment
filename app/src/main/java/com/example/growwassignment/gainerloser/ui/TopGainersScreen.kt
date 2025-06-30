package com.example.growwassignment.gainerloser.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.growwassignment.R
import com.example.growwassignment.gainerloser.marketviewmodels.TopGainerLosersViewModel
import com.example.growwassignment.gainerloser.uistate.UiState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopGainersScreen(topGainerLosersViewModel : TopGainerLosersViewModel,navController: NavHostController){

    var isRefreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        topGainerLosersViewModel.loadTopMarket()
    }
    val topGainersUiState by topGainerLosersViewModel.topMarketGainersUiState.collectAsState()
    fun refresh(){
        refreshScope.launch {
            isRefreshing = true
            topGainerLosersViewModel.loadTopMarket()
            isRefreshing = false
        }
    }
    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = {
                    Text(text = stringResource(R.string.top_gainers))
                })
                HorizontalDivider(modifier = Modifier.fillMaxWidth().background(Color.LightGray) , thickness = 1.dp)
            }
        }
    ) { paddingValues ->

        SwipeRefresh(
            state = SwipeRefreshState(isRefreshing),
            onRefresh = { refresh() },
            modifier = Modifier.padding(paddingValues)
        ) {


            when(topGainersUiState){
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Error -> {
                    val errorMessage = (topGainersUiState as UiState.Error).errorMessage
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = errorMessage, color = Color.Red)
                    }
                }

                is UiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No data available")
                    }
                }
                is UiState.Success -> {
                    val data = (topGainersUiState as UiState.Success).data
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item{
                            ShowStockGrid(data , navController)
                        }
                    }
                }
            }

        }

    }
}



//@Composable
//fun AllTopGainerStock(gainers: List<StockItem>? , navController: NavHostController) {
//
//    val list = gainers ?: emptyList()
//
//    val stockRow = list.chunked(2)
//
//    Column(
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        stockRow.forEach { rowItems ->
//            androidx.compose.foundation.layout.Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                for (item in rowItems) {
//                    Box(modifier = Modifier.weight(1f)) {
//                        TopGainerSingleStock(item , onStockItemClick = {
////                            Log.d("TickerAndPrice","Top Gainer Ticker is ${item.ticker} price is ${item.price}")
//                            navController.navigate(
//                                Screen.StockDetails.createRoute(
//                                    item.ticker,
//                                    item.price.toDouble()
//                                )
//                            )
//                        })
//                    }
//                }
//                if (rowItems.size < 2) {
//                    Spacer(modifier = Modifier.weight(1f))
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun TopGainerSingleStock(stock: StockItem , onStockItemClick : () -> Unit ) {
//    Card(
//        modifier = Modifier
//            .padding(4.dp),
//        elevation = CardDefaults.cardElevation(),
//        onClick = {
//            onStockItemClick()
//        }
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth(),
//            horizontalAlignment = Alignment.Start
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(48.dp)
//                    .clip(CircleShape)
//                    .background(Color.LightGray)
//            ) {
//
//
//                Icon(
//                    modifier = Modifier.fillMaxSize(),
//                    painter = painterResource(R.drawable.top_gainers),
//                    contentDescription = null,
//                    tint = Color.Green
//                )
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = stock.ticker, style = MaterialTheme.typography.bodyMedium)
//            Text(text = "$" + stock.price, style = MaterialTheme.typography.bodySmall)
//        }
//    }
//}
