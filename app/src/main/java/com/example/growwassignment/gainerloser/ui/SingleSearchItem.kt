package com.example.growwassignment.gainerloser.ui

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
import com.example.growwassignment.R
import com.example.growwassignment.gainerloser.marketdatamodels.StockSearchItem


@Composable
fun SingleSearchedItem(stockItem: StockSearchItem) {
    Card(
        modifier = Modifier
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            // This is disable because if i try to navigate to company overview for a click stock item  ,
            // Company overview need price field which is not available in this search endpoint
            // that's why this click is disabled
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


                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.top_gainers),
                    contentDescription = null,
                    tint = Color.Gray
                )
            }


            Spacer(modifier = Modifier.height(8.dp))

            Text(text = stockItem.name, style = MaterialTheme.typography.bodyMedium)
            Text(text = "$" + stockItem.symbol, style = MaterialTheme.typography.bodySmall)

        }
    }
}