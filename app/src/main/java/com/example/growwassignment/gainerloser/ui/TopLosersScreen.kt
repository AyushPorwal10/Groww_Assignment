package com.example.growwassignment.gainerloser.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.growwassignment.R
import com.example.growwassignment.gainerloser.marketviewmodels.TopGainerLosersViewModel
import com.example.growwassignment.gainerloser.uistate.ShowErrorState
import com.example.growwassignment.gainerloser.uistate.ShowLoadingState
import com.example.growwassignment.gainerloser.uistate.ShowUserThatNoDataAvailable
import com.example.growwassignment.gainerloser.uistate.UiState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopLosersScreen(topGainersLosersViewModel: TopGainerLosersViewModel, navController: NavHostController,  ){

    val topLosersUiState by topGainersLosersViewModel.topMarketLosersUiState.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        topGainersLosersViewModel.loadTopMarket()
    }

    fun refresh(){
        refreshScope.launch {
            isRefreshing = true
            topGainersLosersViewModel.loadTopMarket()
            isRefreshing = false
        }
    }


    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = {
                    Text(text = stringResource(R.string.top_losers))
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

            when(topLosersUiState){
                is UiState.Loading -> {
                    ShowLoadingState()
                }
                is UiState.Error -> {
                    val errorMessage = (topLosersUiState as UiState.Error).errorMessage
                    ShowErrorState(errorMessage)
                }

                is UiState.Empty -> {
                    ShowUserThatNoDataAvailable()
                }
                is UiState.Success -> {
                    val topLosersData = (topLosersUiState as UiState.Success).data
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            ShowStockGrid(topLosersData , navController)
                        }
                    }
                }
            }
        }
    }
}


//
//@Composable
//fun AllTopLosersStock(gainers: List<StockItem>? , navController: NavHostController) {
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
//                        TopLosersSingleStock(item , onStockItemClick = {
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
//fun TopLosersSingleStock(stock: StockItem , onStockItemClick : () -> Unit ) {
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
//                    tint = Color.Red
//                )
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = stock.ticker, style = MaterialTheme.typography.bodyMedium)
//            Text(text = "$" + stock.price, style = MaterialTheme.typography.bodySmall)
//        }
//    }
//}
