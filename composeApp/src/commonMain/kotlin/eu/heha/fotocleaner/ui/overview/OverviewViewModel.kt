package eu.heha.fotocleaner.ui.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.heha.fotocleaner.FotoCleanerApp.remoteRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OverviewViewModel : ViewModel() {

    var state by mutableStateOf(OverviewState())
        private set

    fun load() {
        viewModelScope.launch(Dispatchers.Default) {
            state = state.copy(isLoading = true)
            try {
                val result = remoteRepository.listMediaFiles()
                state = state.copy(isLoading = false)
            } catch (e: Exception) {
                Napier.e(e) { "error while loading stuff" }
                state = state.copy(error = e.message ?: "Unknown error", isLoading = false)
            }
        }
    }
}
