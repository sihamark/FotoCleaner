package eu.heha.fotocleaner.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import eu.heha.fotocleaner.ui.ProgressDialog
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginState,
    actions: LoginActions = LoginActions()
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.url,
                onValueChange = actions.onChangeUrl,
                label = { Text("URL") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.userName,
                onValueChange = actions.onChangeUserName,
                label = { Text("User Name") },
                modifier = Modifier.fillMaxWidth()
            )
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

            Button(
                onClick = actions.onClickLogin,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text("Login")
            }
        }
    }

    if (state.isLoading) {
        ProgressDialog("Logging in...")
    }
}

data class LoginState(
    val url: String = "",
    val userName: String = "",
    val password: String = "",
    val error: String? = null,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false
)

class LoginActions(
    val onChangeUrl: (String) -> Unit = {},
    val onChangeUserName: (String) -> Unit = {},
    val onChangePassword: (String) -> Unit = {},
    val onClickLogin: () -> Unit = {}
)

@Preview
@Composable
private fun LoginPreview() {
    LoginScreen(state = LoginState(isLoading = true))
}
