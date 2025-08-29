package eu.heha.fotocleaner

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object FotoCleanerApp {

    private var appValues: AppValues? = null

    val remoteRepository: RemoteRepository
        get() = requireAppValues().remoteRepository

    private fun requireAppValues() =
        appValues ?: error("App not initialized, please call FotoCleanerApp.initialize() first")

    fun initialize(remoteRepository: RemoteRepository) {
        Napier.base(DebugAntilog())
        appValues = AppValues(remoteRepository)
    }

    class AppValues(
        val remoteRepository: RemoteRepository
    )
}