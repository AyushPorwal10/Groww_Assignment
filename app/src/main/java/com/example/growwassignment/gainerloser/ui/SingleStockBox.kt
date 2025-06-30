package com.example.growwassignment.gainerloser.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.growwassignment.R
import com.example.growwassignment.gainerloser.marketdatamodels.StockItem
import com.example.growwassignment.appnavigation.mainactivitynavigation.Screen

@Composable
fun SingleStockBox(stockItem: StockItem ,  navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(),
        onClick = {
            Log.d("TickerAndPrice", "Home Screen Ticker is ${stockItem.ticker} price is ${stockItem.price}")
            navController.navigate(
                Screen.StockDetails.createRoute(
                    stockItem.ticker,
                    stockItem.price.toDouble()
                )
            )
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
                val changePercent =
                    stockItem.change_percentage.substringBefore("%").toDoubleOrNull() ?: 0.0
                val iconRes =
                    if (changePercent < 0) R.drawable.top_losers else R.drawable.top_gainers
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = if (iconRes == R.drawable.top_gainers) Color.Green else Color.Red
                )
            }


            Spacer(modifier = Modifier.height(8.dp))

            Text(text = stockItem.ticker, style = MaterialTheme.typography.bodyMedium)
            Text(text = "$" + stockItem.price, style = MaterialTheme.typography.bodySmall)

        }
    }
}