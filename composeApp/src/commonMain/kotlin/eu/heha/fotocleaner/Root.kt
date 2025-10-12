package eu.heha.fotocleaner

import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import eu.heha.fotocleaner.ui.input.InputRoute
import eu.heha.fotocleaner.ui.overview.OverviewRoute
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
@Preview
fun Root() {
    MaterialExpressiveTheme(
        motionScheme = MotionScheme.expressive()
    ) {
        var current by remember { mutableStateOf(NavigationDestination.Login) }
        AnimatedContent(current) {
            when (it) {
                NavigationDestination.Login -> InputRoute(
                    onLoginSuccess = { current = NavigationDestination.Overview }
                )

                NavigationDestination.Overview -> OverviewRoute(
                    onLogout = { current = NavigationDestination.Login }
                )
            }
        }
    }
}

enum class NavigationDestination {
    Login, Overview
}