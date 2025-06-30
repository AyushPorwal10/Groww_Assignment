package com.example.growwassignment.gainerloser.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.growwassignment.watchlist.roomentity.Watchlist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistBottomSheet(
    showSheet: Boolean,
    onDismissRequest: () -> Unit,
    watchlists: List<Watchlist>,
    onAddNewWatchlist: (String) -> Unit,
    onWatchlistSelected: (Watchlist) -> Unit
) {

    if (showSheet) {
        var newListName by remember { mutableStateOf("") }

        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
        ) {

            Text(
                "Your Watchlists",
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = newListName,
                        onValueChange = { newListName = it },
                        label = { Text("New Watchlist Name") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = {
                            if (newListName.isNotBlank()) {
                                onAddNewWatchlist(newListName)
                                newListName = ""
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.Black)
                    ) {
                        Text("Add", color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                LazyColumn {
                    items(watchlists) { list ->
                        Text(
                            text = list.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onWatchlistSelected(list)
                                    onDismissRequest()
                                }
                                .padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}
