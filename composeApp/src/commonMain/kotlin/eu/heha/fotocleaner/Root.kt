package eu.heha.fotocleaner

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import eu.heha.fotocleaner.ui.login.LoginRoute
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun Root() {
    MaterialTheme {
        LoginRoute()
    }
}