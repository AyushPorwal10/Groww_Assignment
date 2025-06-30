package com.example.growwassignment.gainerloser.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.growwassignment.R
import com.example.growwassignment.appnavigation.mainactivitynavigation.Screen
import com.example.growwassignment.gainerloser.marketdatamodels.StockItem
import com.example.growwassignment.gainerloser.marketdatamodels.StockSearchItem
import com.example.growwassignment.gainerloser.marketviewmodels.MarketViewModel
import com.example.growwassignment.gainerloser.uistate.ShowEmptyState
import com.example.growwassignment.gainerloser.uistate.ShowErrorState
import com.example.growwassignment.gainerloser.uistate.ShowLoadingState
import com.example.growwassignment.gainerloser.uistate.StockSearchUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTickerScreen(marketViewModel: MarketViewModel, navController: NavHostController) {

    val searchUiState by marketViewModel.searchQueryUiState.collectAsState()
    val searchText = remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = {
                    Text(text = stringResource(R.string.search))
                })
                HorizontalDivider(thickness = 2.dp)
            }

        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(10.dp)
        ) {

            OutlinedTextField(
                value = searchText.value,
                onValueChange = {
                    searchText.value = it
                    marketViewModel.onSearchQueryChanged(it)
                },
                label = { Text("Search here...") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            when (searchUiState) {
                is StockSearchUiState.Loading -> {
                    ShowLoadingState()
                }

                is StockSearchUiState.Success -> {
                    val searchResult = (searchUiState as StockSearchUiState.Success).items
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            ShowSearchedStockGrid(searchResult)
                        }
                    }
                }

                is StockSearchUiState.Error -> {
                    val errorMessage = (searchUiState as StockSearchUiState.Error).message
                    ShowErrorState(errorMessage)
                }

                is StockSearchUiState.Empty -> {
                    ShowEmptyState()
                }

                else -> {}
            }
        }
    }
}

