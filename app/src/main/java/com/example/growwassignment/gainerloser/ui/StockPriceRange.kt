package com.example.growwassignment.gainerloser.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.growwassignment.gainerloser.marketdatamodels.CompanyOverviewData

@Composable
fun StockPriceRange(
    companyOverviewData: CompanyOverviewData,
    sortedPrices: List<Pair<String, Float>>
) {
    val low = companyOverviewData.weekLow52?.toFloatOrNull() ?: 0.0f
    val high = companyOverviewData.weekHigh52?.toFloatOrNull() ?: 0.0f
    val current = sortedPrices.firstOrNull()?.second ?: 0.0f

    val progress = ((current - low) / (high - low)).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            val lineWidthPx = constraints.maxWidth.toFloat()
            val density = LocalDensity.current
            val offset = with(density) { (progress * lineWidthPx).toDp() }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = offset - 40.dp) // need adjust to center arrow
                        .align(Alignment.TopStart)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Current Price: $${String.format("%.2f", current)}",
                            fontSize = 12.sp
                        )
                        Text("▼", fontSize = 14.sp)
                    }
                }
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(2.dp)
            ) {
                val y = size.height / 2
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 4f
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text("52-Week Low", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "$${String.format("%.2f", low)}",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("52-Week High", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "$${String.format("%.2f", high)}",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
