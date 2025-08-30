package eu.heha.fotocleaner.ui.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.heha.fotocleaner.ui.ProgressDialog
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OverviewScreen(
    state: OverviewState,
    actions: OverviewActions = OverviewActions()
) {
    Scaffold { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (!state.isLoading) {
                if (state.error != null) {
                    ErrorDisplay(
                        error = state.error,
                        onClickRetryLoad = actions.onClickRetryLoad
                    )
                } else {
                    Text(
                        "You have ${state.amountOfImageFile} image files.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
    if (state.isLoading) {
        ProgressDialog("Loading Files...")
    }
}

@Composable
private fun ErrorDisplay(
    error: String,
    onClickRetryLoad: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Error: $error",
            style = MaterialTheme.typography.bodyLarge
        )
        OutlinedButton(onClickRetryLoad) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Retry"
            )
            Spacer(Modifier.width(8.dp))
            Text("Retry")
        }
    }
}

data class OverviewState(
    val amountOfImageFile: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class OverviewActions(
    val onClickRetryLoad: () -> Unit = {}
)

@Preview
@Composable
fun OverviewPreview() {
    MaterialTheme {
        OverviewScreen(state = OverviewState(amountOfImageFile = 1234))
    }
}