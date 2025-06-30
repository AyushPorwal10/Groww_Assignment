package com.example.growwassignment.gainerloser.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.growwassignment.gainerloser.marketdatamodels.StockSearchItem


@Composable
fun ShowSearchedStockGrid(stockItems: List<StockSearchItem>) {

    val stockRow = stockItems.chunked(2)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        stockRow.forEach { rowItems ->
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                for (item in rowItems) {
                    Box(modifier = Modifier.weight(1f)) {
                        SingleSearchedItem(item)
                    }
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}