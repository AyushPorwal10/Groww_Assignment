package com.example.growwassignment.gainerloser.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.growwassignment.R
import com.example.growwassignment.gainerloser.marketviewmodels.MarketViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTickerScreen(marketViewModel: MarketViewModel, navController: NavHostController) {

    val searchedResult = marketViewModel.searchResults.collectAsState()
    val searchText = remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = {
                    Text(text = stringResource(R.string.search))
                })
                HorizontalDivider(thickness = 2.dp)
            }

        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                OutlinedTextField(
                    value = searchText.value,
                    onValueChange = {
                        searchText.value = it

                        marketViewModel.searchByTicker(searchText.value)
                    },
                    label = { Text("Search here...") },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(8.dp)
                )
            }
            item {
                ShowStockGrid(searchedResult.value, navController)
            }
        }
    }
}
//
//@Composable
//fun SearchedStockGrid(searchResults: List<StockItem>, navController: NavHostController) {
//
//
//    val rows = searchResults.chunked(2)
//
//    Column(
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        rows.forEach { rowItems ->
//            androidx.compose.foundation.layout.Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                for (item in rowItems) {
//                    Box(modifier = Modifier.weight(1f)) {
//                        SearchedStockItemBox(item , navController)
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
//fun SearchedStockItemBox(stock: StockItem, navController: NavHostController) {
//    Card(
//        modifier = Modifier
//            .padding(4.dp),
//        elevation = CardDefaults.cardElevation(),
//        onClick = {
//            Log.d("TickerAndPrice", "Home Screen Ticker is ${stock.ticker} price is ${stock.price}")
//            navController.navigate(
//                Screen.StockDetails.createRoute(
//                    stock.ticker,
//                    stock.price.toDouble()
//                )
//            )
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
//                // if change Percent is < 0 that mean losers else gainers
//                val changePercent =
//                    stock.change_percentage.substringBefore("%").toDoubleOrNull() ?: 0.0
//                val iconRes =
//                    if (changePercent < 0) R.drawable.top_losers else R.drawable.top_gainers
//                Icon(
//                    modifier = Modifier.fillMaxSize(),
//                    painter = painterResource(iconRes),
//                    contentDescription = null,
//                    tint = if (iconRes == R.drawable.top_gainers) Color.Green else Color.Red
//                )
//            }
//
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(text = stock.ticker, style = MaterialTheme.typography.bodyMedium)
//            Text(text = "$" + stock.price, style = MaterialTheme.typography.bodySmall)
//
//        }
//    }
//}