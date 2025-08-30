package eu.heha.fotocleaner.ui.overview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun OverviewRoute(onLogout: () -> Unit) {
    val model = viewModel { OverviewViewModel() }
    LaunchedEffect(model) {
        model.load()
    }
    OverviewScreen(model.state)
}

