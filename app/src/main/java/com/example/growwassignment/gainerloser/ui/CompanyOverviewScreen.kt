package com.example.growwassignment.gainerloser.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.growwassignment.R
import com.example.growwassignment.gainerloser.marketdata.CompanyOverviewData
import com.example.growwassignment.gainerloser.marketviewmodels.TopGainerLosersViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

//@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyOverviewScreen(
    companySymbol: String?,
    topGainerLosersViewModel: TopGainerLosersViewModel
) {

    val companyOverviewData = topGainerLosersViewModel.companyOverviewData.collectAsState()


    LaunchedEffect(Unit) {
        if (companySymbol != null) {
            topGainerLosersViewModel.fetchCompanyOverview(companySymbol)
        }
    }
    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = {
                    Text(text = stringResource(R.string.details_screen))
                })
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .background(Color.LightGray), thickness = 1.dp
                )
            }

        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            CompanyNameAndLogo(companyOverviewData)

            StockChartScreen(topGainerLosersViewModel, companySymbol)

            CompanyAllDetails(companyOverviewData)


        }
    }
}

@Composable
fun CompanyNameAndLogo(companyOverviewData: State<CompanyOverviewData?>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                Image(
                    painter = painterResource(R.drawable.top_losers),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Inside,
                    contentDescription = null
                )
            }

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = companyOverviewData.value?.Name ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${companyOverviewData.value?.Symbol} , ${companyOverviewData.value?.AssetType}" ,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = companyOverviewData.value?.Exchange ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "Price",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "+1.25%",
                color = Color.Green,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Composable
fun CompanyAllDetails(companyOverviewData: State<CompanyOverviewData?>) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContentColor = Color.Black,
            disabledContainerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = ("About" + companyOverviewData.value?.Name),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            Text(
                text = companyOverviewData.value?.Description ?: "",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Box(modifier = Modifier.background(Color.Magenta, RoundedCornerShape(20.dp))) {
                    Text(
                        "Industry : ${companyOverviewData.value?.Industry}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Box(modifier = Modifier.background(Color.Magenta, RoundedCornerShape(20.dp))) {
                    Text(
                        "Sector : ${companyOverviewData.value?.Sector}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

    }
}


@Composable
fun StockChartScreen(topGainerLosersViewModel: TopGainerLosersViewModel, companySymbol: String?) {
    val context = LocalContext.current

    val prices = topGainerLosersViewModel.priceData

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
        if (prices.isNotEmpty()) {
            StockLineChart(prices)
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun StockLineChart(prices: List<Pair<String, Float>>) {

    val sortedPrices = remember(prices) {
        prices.sortedBy { it.first }
    }

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
                Color.LightGray
                setDrawValues(false)
                setDrawCircles(false)
                lineWidth = 2f
            }
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(
                prices.map { it.first.substring(11, 16) }
            )
            chart.data = LineData(dataset)
            chart.invalidate()
        },
        modifier = Modifier.fillMaxSize()
    )
}



