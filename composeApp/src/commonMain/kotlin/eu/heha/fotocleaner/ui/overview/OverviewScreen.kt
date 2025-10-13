@file:OptIn(ExperimentalTime::class)

package eu.heha.fotocleaner.ui.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.heha.fotocleaner.model.RemoteRepository
import eu.heha.fotocleaner.ui.ProgressDialog
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

private val maxWidth = 400.dp

@Composable
fun OverviewScreen(
    state: OverviewState,
    actions: OverviewActions = OverviewActions()
) {
    Scaffold { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            item { Spacer(Modifier.padding(16.dp)) }

            if (!state.isLoading) {
                if (state.error != null) {
                    item {
                        ErrorDisplay(
                            error = state.error,
                            onClickRetryLoad = actions.onClickRetryLoad,
                            modifier = Modifier.widthIn(max = maxWidth)
                        )
                    }
                } else {
                    item {
                        Text(
                            "You have ${state.mediaFiles.size} image files.",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.widthIn(max = maxWidth)
                        )
                    }
                    items(state.mediaFiles) { mediaFile ->
                        MediaFileDisplay(
                            mediaFile,
                            modifier = Modifier.widthIn(max = maxWidth)
                        )
                    }
                }
            }
        }
    }
    if (state.isLoading) {
        ProgressDialog("Analysing media files...")
    }
}

@Composable
fun MediaFileDisplay(mediaFile: RemoteRepository.MediaFile, modifier: Modifier = Modifier) {
    Card(modifier.padding(8.dp)) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = mediaFile.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = mediaFile.path,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Date: ${mediaFile.date}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ErrorDisplay(
    error: String,
    onClickRetryLoad: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
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
    val isLoading: Boolean = false,
    val error: String? = null,
    val mediaFiles: List<RemoteRepository.MediaFile> = listOf()
)

class OverviewActions(
    val onClickRetryLoad: () -> Unit = {}
)

@Preview
@Composable
fun OverviewPreview() {
    fun debugMediaFile(name: String) = RemoteRepository.MediaFile(
        name = name,
        path = "/path/to/$name",
        date = Clock.System.now().minus((1..1000).random().minutes),
        subPath = "",
        doesSubPathExist = true
    )
    MaterialTheme {
        OverviewScreen(
            state = OverviewState(
                mediaFiles = listOf(
                    debugMediaFile("1.jpg"),
                    debugMediaFile("2.jpg"),
                    debugMediaFile("3.jpg"),
                    debugMediaFile("4.jpg")
                )
            )
        )
    }
}