package eu.heha.fotocleaner.ui.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import eu.heha.fotocleaner.ui.ProgressDialog
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    state: InputState,
    actions: InputActions = InputActions()
) {
    Scaffold { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Card(
                Modifier
                    .padding(innerPadding)
                    .padding(32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                        .widthIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        "Login",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.url,
                        onValueChange = actions.onChangeUrl,
                        label = { Text("URL") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = state.userName,
                        onValueChange = actions.onChangeUserName,
                        label = { Text("User Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = state.password,
                        onValueChange = actions.onChangePassword,
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )

                    AnimatedVisibility(state.error != null) {
                        Text(
                            text = state.error ?: "",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = actions.onClickUseInputs,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text("Use Inputs")
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.Send, null)
                    }
                }
            }
        }
    }

    if (state.isLoading) {
        ProgressDialog("Logging in...")
    }
}

data class InputState(
    val url: String = "",
    val userName: String = "",
    val password: String = "",
    val error: String? = null,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false
)

class InputActions(
    val onChangeUrl: (String) -> Unit = {},
    val onChangeUserName: (String) -> Unit = {},
    val onChangePassword: (String) -> Unit = {},
    val onClickUseInputs: () -> Unit = {}
)

@Preview
@Composable
private fun LoginPreview() {
    InputScreen(state = InputState(isLoading = true))
}
