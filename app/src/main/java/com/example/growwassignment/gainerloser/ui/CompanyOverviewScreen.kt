package com.example.growwassignment.gainerloser.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.growwassignment.R
import com.example.growwassignment.gainerloser.marketdatamodels.CompanyOverviewData
import com.example.growwassignment.gainerloser.marketviewmodels.TopGainerLosersViewModel
import com.example.growwassignment.gainerloser.uistate.ShowErrorState
import com.example.growwassignment.gainerloser.uistate.ShowLoadingState
import com.example.growwassignment.gainerloser.uistate.ShowUserThatNoDataAvailable
import com.example.growwassignment.gainerloser.uistate.UiState
import com.example.growwassignment.watchlist.uistate.WatchlistUiState
import com.example.growwassignment.watchlist.roomviewmodel.WatchlistViewModel


//@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyOverviewScreen(
    companySymbol: String?,
    companyPrice: Double?,
    topGainerLosersViewModel: TopGainerLosersViewModel,
    watchlistViewModel: WatchlistViewModel
) {


    val snackbarHostState = remember { SnackbarHostState() }

    val prices = topGainerLosersViewModel.priceData

    val companyOverviewUiState by topGainerLosersViewModel.companyOverviewUiState.collectAsState()

    val watchlistUiState by watchlistViewModel.watchlistUiState.collectAsState()



    val isCompanyStockSaved = companySymbol?.let {
        watchlistViewModel.isCompanyStockSaved(it).collectAsState(initial = false).value
    } ?: false

    val sortedPrices = remember(prices) {
        prices.sortedBy { it.first }
    }

    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (companySymbol != null) {
            topGainerLosersViewModel.fetchCompanyOverview(companySymbol)
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TopAppBar(
                        title = {
                            Text(text = stringResource(R.string.details_screen))
                        },
                        actions = {
                            Icon(
                                painter = painterResource(if (isCompanyStockSaved) R.drawable.saved else R.drawable.unsaved),
                                contentDescription = stringResource(
                                    R.string.save
                                ),
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable {
                                        // if already saved and again click than unsave it
                                        if(isCompanyStockSaved){
                                            Log.d("WATCHLIST", "SYMBOL ($companySymbol) is Saved now unsaving it ")
                                            companySymbol?.let {
                                                watchlistViewModel.unsaveWatchlistItem(companySymbol)
                                            }
                                        }else {
                                            showBottomSheet = true
                                        }

                                    }
                            )
                        }
                    )
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .background(Color.LightGray), thickness = 1.dp
                )
            }

        }
    ) { paddingValues ->

        if (showBottomSheet) {


            WatchlistBottomSheet(
                showSheet = true,
                onDismissRequest = { showBottomSheet = false },
                watchlists = watchlistViewModel.showAllWatchlist.collectAsState(emptyList()).value, // showing all watchlist in bottom sheet pop up
                onAddNewWatchlist = { name ->
                    watchlistViewModel.addWatchlist(name)  // when user add new watchlist
                },
                onWatchlistSelected = { selected ->
                    watchlistViewModel.addItemToWatchlist(
                        ticker = companySymbol ?: "",
                        price = companyPrice ?: 0.0,
                        watchlistId = selected.id       // this will be used to identify which watchlist user selected to add item
                    )
                }
            )
        }

        when(companyOverviewUiState){
            is UiState.Loading -> {
                ShowLoadingState()
            }
            is UiState.Empty -> {
                ShowUserThatNoDataAvailable()
            }
            is UiState.Error -> {
                val errorMessage = (companyOverviewUiState as UiState.Error).errorMessage
                ShowErrorState(errorMessage)
            }
            is UiState.Success -> {

                val overviewData = (companyOverviewUiState as UiState.Success).data

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                ) {

                    // this include company name , asset type , symbol and current price using first index of sorted prices
                    CompanyNameAndLogo(overviewData, sortedPrices)

                    // price graph
                    StockChartScreen(topGainerLosersViewModel, companySymbol, sortedPrices)

                    // description , industry , price range , and few other details
                    CompanyAllDetails(overviewData, sortedPrices)
                }
            }
        }


        LaunchedEffect(watchlistUiState) {
            when(val state = watchlistUiState){
                is WatchlistUiState.SuccessMessage -> {
                    snackbarHostState.showSnackbar(
                        message =  state.successMessage,
                        actionLabel = "Ok")
                    watchlistViewModel.clearUiState()
                }
                is WatchlistUiState.Error -> {
                    snackbarHostState.showSnackbar(
                        message = state.errorMessage ,
                        actionLabel = "Ok")
                    watchlistViewModel.clearUiState()
                }
                else -> {

                   // Log.d("WATCHLIST", "Else part should be shown")
                }
            }
        }

    }
}

@Composable
fun CompanyNameAndLogo(
    companyOverviewData: CompanyOverviewData,
    sortedPrices: List<Pair<String, Float>>
) {


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
                    text = companyOverviewData.Name ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${companyOverviewData.Symbol} , ${companyOverviewData.AssetType}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = companyOverviewData.Exchange ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }


        if (sortedPrices.isNotEmpty()) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${sortedPrices[sortedPrices.size-1].second}", // taking last because it will be latest price
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                // No such field that shows % increase
//                Text(
//                    text = "+1.25%",
//                    color = Color.Green,
//                    style = MaterialTheme.typography.titleSmall,
//                    fontWeight = FontWeight.Medium
//                )
            }
        }

    }
}



