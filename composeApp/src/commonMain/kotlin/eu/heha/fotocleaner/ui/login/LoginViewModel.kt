package eu.heha.fotocleaner.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.heha.fotocleaner.FotoCleanerApp.remoteRepository
import io.github.aakira.napier.Napier
import io.ktor.http.Url
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class LoginViewModel : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    fun load() {
        viewModelScope.launch {
            remoteRepository.getStoredCredentials()?.let {
                state = state.copy(
                    url = it.url,
                    userName = it.userName,
                    password = it.password,
                    error = null
                )
            }
        }
    }

    fun onChangeUrl(newUrl: String) {
        state = state.copy(url = newUrl, error = null)
    }

    fun onChangeUserName(newUserName: String) {
        state = state.copy(userName = newUserName, error = null)
    }

    fun onChangePassword(newPassword: String) {
        state = state.copy(password = newPassword, error = null)
    }

    fun login() {
        if (state.url.isBlank() || state.userName.isBlank() || state.password.isBlank()) {
            state = state.copy(error = "All fields are required")
            return
        }
        state = state.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val waitJob = launch { delay(2.seconds) }
            try {
                remoteRepository.login(
                    url = Url(state.url).toString(),
                    userName = state.userName,
                    password = state.password
                )
                state = state.copy(isLoading = false, isSuccess = true, error = null)
                Napier.e { "successfully logged in" }
            } catch (e: Exception) {
                Napier.e(e) { "error logging in" }
                waitJob.join()
                state = state.copy(isLoading = false, error = e.message)
            }
        }
    }
}
