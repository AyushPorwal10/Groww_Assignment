package com.example.growwassignment.watchlist.uistate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun ShowMessageToUser(message : String) {

    val snackBarHostState = remember {SnackbarHostState()}

    LaunchedEffect(message) {
        if(message.isNotBlank()){
            snackBarHostState.showSnackbar(
                message = message,
                actionLabel = "OK"
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(hostState = snackBarHostState)
    }
}

