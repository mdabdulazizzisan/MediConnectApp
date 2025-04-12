package com.kolu.mediconnect.presentation.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.kolu.mediconnect.navigation.StartDestinationViewModel
import com.kolu.mediconnect.presentation.screens.LoadingScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    startDestinationViewModel: StartDestinationViewModel = koinViewModel(),
    onNavigateToLoginScreen: () -> Unit = {},
    onBookAnAppointmentClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val startDestinationViewState = startDestinationViewModel.viewState.collectAsState()
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when(startDestinationViewState.value) {
            StartDestinationViewModel.StartDestinationViewState.Loading -> {
                LoadingScreen()
                println("viewState: Loading")
            }
            StartDestinationViewModel.StartDestinationViewState.NotLoggedIn -> {
                LaunchedEffect(startDestinationViewState) {
                    onNavigateToLoginScreen()
                }
            }
            StartDestinationViewModel.StartDestinationViewState.LoggedIN -> {
                HomeContent(
                    onBookAnAppointmentClick = {
                        onBookAnAppointmentClick()
                    },
                    onProfileClick = {
                        onProfileClick()
                    }
                )

            }
        }

    }
    BackHandler {
        android.os.Process.killProcess(android.os.Process.myPid())
    }
}
