package eu.heha.fotocleaner.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.aakira.napier.Napier

@Composable
fun LoginRoute() {
    val model = viewModel { LoginViewModel() }
    LaunchedEffect(model.state.isSuccess) {
        if (model.state.isSuccess) {
            Napier.e { "successfully logged in, go further" }
        }
    }
    LoginScreen(
        state = model.state,
        actions = LoginActions(
            onChangeUrl = model::onChangeUrl,
            onChangeUserName = model::onChangeUserName,
            onChangePassword = model::onChangePassword,
            onClickLogin = model::login
        )
    )
}