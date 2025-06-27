package com.example.growwassignment.gainerloser.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.growwassignment.R
import com.example.growwassignment.gainerloser.marketdata.StockItem
import com.example.growwassignment.gainerloser.marketviewmodels.TopGainerLosersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopGainersScreen(topGainerLosersViewModel : TopGainerLosersViewModel, onStockItemClick: (String) -> Unit){
    val topGainersData = topGainerLosersViewModel.topMarketGainersData.collectAsState()

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

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item{
                AllTopGainerStock(topGainersData.value , onStockItemClick = {
                    onStockItemClick(it)
                })
            }

        }
    }
}



@Composable
fun AllTopGainerStock(gainers: List<StockItem>? , onStockItemClick: (String) -> Unit) {

    val list = gainers ?: emptyList()

    val rows = list.chunked(2)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        rows.forEach { rowItems ->
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                for (item in rowItems) {
                    Box(modifier = Modifier.weight(1f)) {
                        TopGainerSingleStock(item , onStockItemClick = {
                            onStockItemClick(item.ticker)// passing company name to get company overview
                        })
                    }
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun TopGainerSingleStock(stock: StockItem , onStockItemClick : () -> Unit ) {
    Card(
        modifier = Modifier
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(),
        onClick = {
            onStockItemClick()
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


                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.top_gainers),
                    contentDescription = null,
                    tint = Color.Green
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stock.ticker, style = MaterialTheme.typography.bodyMedium)
            Text(text = "$" + stock.price, style = MaterialTheme.typography.bodySmall)
        }
    }
}
