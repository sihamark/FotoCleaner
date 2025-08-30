package eu.heha.fotocleaner.ui.input

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.aakira.napier.Napier

@Composable
fun InputRoute(onLoginSuccess: () -> Unit) {
    val model = viewModel { InputViewModel() }
    LaunchedEffect(model) {
        model.load()
    }
    LaunchedEffect(model.state.isSuccess) {
        if (model.state.isSuccess) {
            Napier.e { "successfully logged in, go further" }
            onLoginSuccess()
        }
    }
    InputScreen(
        state = model.state,
        actions = InputActions(
            onChangeUrl = model::onChangeUrl,
            onChangeUserName = model::onChangeUserName,
            onChangePassword = model::onChangePassword,
            onClickUseInputs = model::useInputs
        )
    )
}