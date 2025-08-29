package eu.heha.fotocleaner

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    FotoCleanerApp.initialize(SardineRemoteRepository())
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "FotoCleaner",
        ) {
            App()
        }
    }
}