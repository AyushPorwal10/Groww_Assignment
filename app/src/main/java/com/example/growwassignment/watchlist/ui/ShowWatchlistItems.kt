package com.example.growwassignment.watchlist.ui

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.growwassignment.R
import com.example.growwassignment.appnavigation.mainactivitynavigation.Screen
import com.example.growwassignment.gainerloser.uistate.ShowLoadingState
import com.example.growwassignment.watchlist.roomentity.WatchlistItem
import com.example.growwassignment.watchlist.uistate.WatchlistUiState
import com.example.growwassignment.watchlist.roomviewmodel.WatchlistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowWatchlistsItems(
    watchlistViewModel: WatchlistViewModel,
    navHostController: NavHostController,
    watchlistId: Int?,
    watchlistName: String?
) {

    LaunchedEffect(Unit) {
        if (watchlistId != null)
            watchlistViewModel.loadItemsFromWatchlist(watchlistId)
        else {
            Log.d("WATCHLIST","ShowWatchListItems WATCHLIST id is null ")
        }
    }

    val watchlistItemUiState by watchlistViewModel.watchlistUiState.collectAsState()


    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = {
                    Text(watchlistName ?: "Watchlist", fontWeight = FontWeight.SemiBold)
                })
                HorizontalDivider(thickness = 2.dp)
            }

        }
    ) { paddingValues ->

        when (val state = watchlistItemUiState) {
            is WatchlistUiState.Success -> {
                Log.d("WATCHLIST","Show watch list item success")
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item{
                        StockGrid(state.data, navHostController)
                    }
                }
            }

            is WatchlistUiState.Loading -> {
                Log.d("WATCHLIST","Show watch list item loading")
                ShowLoadingState()
            }

            is WatchlistUiState.Empty -> {
                Log.d("WATCHLIST","Show watch list item empty")
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center) {
                    Text(
                        "No items found.",
                        textAlign = TextAlign.Center
                    )
                }
            }

            is WatchlistUiState.Idle -> {
                Log.d("WATCHLIST","Show watch list item idle")
                //
            }

            else -> {
                Log.d("WATCHLIST","Show watch list item else part")
                //
            }
        }

    }
}

@Composable
fun StockGrid(watchlistItem: List<WatchlistItem>, navHostController: NavHostController) {

    val list = watchlistItem ?: emptyList()

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
                        SingleWatchlistItem(item, navHostController)
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
fun SingleWatchlistItem(watchlistItem: WatchlistItem, navHostController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            navHostController.navigate(
                Screen.StockDetails.createRoute(
                    watchlistItem.ticker,
                    watchlistItem.price
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


                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.top_gainers),
                    contentDescription = null,
                    tint = Color.Green
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = watchlistItem.ticker, style = MaterialTheme.typography.bodyMedium)
            Text(text = "$" + watchlistItem.price, style = MaterialTheme.typography.bodySmall)
        }
    }
}