package com.example.growwassignment.gainerloser.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.growwassignment.gainerloser.marketdatamodels.CompanyOverviewData
import com.example.growwassignment.ui.theme.LightOrange
import com.example.growwassignment.ui.theme.LightOrangeText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompanyAllDetails(
    companyOverviewData: CompanyOverviewData,
    sortedPrices: List<Pair<String, Float>>,
) {

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
                text = ("About " + "${companyOverviewData.Name}"),
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
                text = "${companyOverviewData.Description}",
                style = MaterialTheme.typography.bodyMedium,

                )

            Spacer(Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(LightOrange, RoundedCornerShape(20.dp))
                ) {
                    Text(
                        text = "Industry: ${companyOverviewData.Industry}",
                        style = MaterialTheme.typography.bodySmall,
                        color = LightOrangeText,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .background(LightOrange, RoundedCornerShape(20.dp))
                ) {
                    Text(
                        text = "Sector: ${companyOverviewData.Sector}",
                        style = MaterialTheme.typography.bodySmall,
                        color = LightOrangeText,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }


            Spacer(Modifier.height(8.dp))


            StockPriceRange(companyOverviewData, sortedPrices)

            Spacer(Modifier.height(8.dp))

            // marker cap , p/e ratio , beta , dividend yield , profit margin
            CompanyOtherDetails(companyOverviewData)
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompanyOtherDetails(companyOverviewData: CompanyOverviewData) {

    FlowRow(
        modifier = Modifier.padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column {
            Text("Marker Cap", style = MaterialTheme.typography.bodyMedium)
            Text(
                "${companyOverviewData.MarketCapitalization}", fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Column {
            Text("P/E Ratio", style = MaterialTheme.typography.bodyMedium)
            Text(
                "${companyOverviewData.PERatio}", fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Column {
            Text("Beta", style = MaterialTheme.typography.bodyMedium)
            Text(
                "${companyOverviewData.Beta}", fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Column {
            Text("Dividend Yield", style = MaterialTheme.typography.bodyMedium)
            Text(
                "${companyOverviewData.DividendYield}", fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Column {
            Text("Profit Margin", style = MaterialTheme.typography.bodyMedium)
            Text(
                "${companyOverviewData.ProfitMargin}", fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}