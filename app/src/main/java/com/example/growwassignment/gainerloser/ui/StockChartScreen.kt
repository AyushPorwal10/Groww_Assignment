package com.example.growwassignment.gainerloser.ui

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.growwassignment.gainerloser.marketviewmodels.TopGainerLosersViewModel
import com.example.growwassignment.gainerloser.uistate.ShowUserThatNoDataAvailable
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StockChartScreen(
    topGainerLosersViewModel: TopGainerLosersViewModel,
    companySymbol: String?,
    sortedPrices: List<Pair<String, Float>>
) {
    val context = LocalContext.current

    if (companySymbol == null) {
        Toast.makeText(
            context,
            "Something went wrong \n Unable to load Price Graph",
            Toast.LENGTH_SHORT
        ).show()
        return
    }
    LaunchedEffect(Unit) {
        topGainerLosersViewModel.loadPricesGraph(companySymbol)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(300.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContentColor = Color.Black,
            disabledContainerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        if (sortedPrices.isNotEmpty()) {
            StockLineChart(sortedPrices)
        } else {
            ShowUserThatNoDataAvailable()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StockLineChart(sortedPrices: List<Pair<String, Float>>) {


    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                this.setTouchEnabled(true)
                this.setScaleEnabled(true)
                this.description.isEnabled = false
                this.axisRight.isEnabled = false
                this.xAxis.position = XAxis.XAxisPosition.BOTTOM
                setBackgroundColor(android.graphics.Color.WHITE)
                xAxis.setDrawGridLines(false)
                axisLeft.setDrawGridLines(false)

                setDrawBorders(false)
            }
        },
        update = { chart ->

            val entries = sortedPrices.mapIndexed { index, pair ->
                Entry(index.toFloat(), pair.second)
            }
            val dataset = LineDataSet(entries, "Close Price").apply {
                setColor(Color.LightGray.toArgb())
                setDrawValues(false)
                setDrawCircles(false)
                lineWidth = 2f
            }

            chart.axisLeft.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "$" + String.format("%.2f", value)
                }
            }


            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())

            val timeLabels = sortedPrices.map { (timestamp, _) ->
                try {
                    val time = LocalDateTime.parse(timestamp, inputFormatter)
                    time.format(outputFormatter)
                } catch (e: Exception) {
                    timestamp
                }
            }

            chart.xAxis.valueFormatter = IndexAxisValueFormatter(timeLabels)
            chart.data = LineData(dataset)
            chart.invalidate()
        },
        modifier = Modifier.fillMaxSize()
    )
}
