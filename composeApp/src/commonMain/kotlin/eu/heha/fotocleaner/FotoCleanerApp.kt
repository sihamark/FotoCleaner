package eu.heha.fotocleaner

import androidx.compose.runtime.Composable
import eu.heha.fotocleaner.model.RemoteRepository
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.io.files.Path

object FotoCleanerApp {

    private var appValues: AppValues? = null

    val filesRoot: Path
        get() = requireAppValues().filesRoot

    val remoteRepository: RemoteRepository
        get() = requireAppValues().remoteRepository

    private fun requireAppValues() =
        appValues ?: error("App not initialized, please call FotoCleanerApp.initialize() first")

    fun initialize(
        antilog: Antilog = DebugAntilog(),
        filesRoot: Path,
        remoteRepository: RemoteRepository
    ) {
        Napier.base(antilog)
        appValues = AppValues(
            filesRoot = filesRoot,
            remoteRepository = remoteRepository
        )
    }

    @Composable
    fun App() {
        Root()
    }

    class AppValues(
        val filesRoot: Path,
        val remoteRepository: RemoteRepository
    )
}