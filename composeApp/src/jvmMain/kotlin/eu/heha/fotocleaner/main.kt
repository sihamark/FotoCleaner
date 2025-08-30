package eu.heha.fotocleaner

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.ktor.http.encodeURLPath
import kotlinx.io.files.Path
import java.awt.Dimension
import java.io.File

fun main() {
    FotoCleanerApp.initialize(
        antilog = Logging.antilog(),
        filesRoot = filesRoot(),
        remoteRepository = SardineRemoteRepository()
    )
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "FotoCleaner",
        ) {
            LaunchedEffect(window) {
                window.minimumSize = Dimension(300, 500)
            }
            FotoCleanerApp.App()
        }
    }
}

private fun filesRoot(): Path {
    val jarFilePath = FotoCleanerApp::class.java.protectionDomain.codeSource.location.file
        .encodeURLPath()

    val rootFile = File(jarFilePath)
        .parentFile //app folder
        .parentFile //root folder

    val dataFolder = File(rootFile, "data")
        .also { it.mkdirs() }

    return kotlinx.io.files.Path(dataFolder.path)
}