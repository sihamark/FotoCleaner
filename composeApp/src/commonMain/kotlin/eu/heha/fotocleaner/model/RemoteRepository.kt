@file:OptIn(ExperimentalTime::class)

package eu.heha.fotocleaner.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface RemoteRepository {

    suspend fun loadFiles(url: String, userName: String, password: String)

    suspend fun getStoredCredentials(): CredentialsStore.Credentials?

    suspend fun listMediaFiles(): AnalysisResult

    data class AnalysisResult(
        val directory: String,
        val mediaFiles: List<MediaFile>,
    )

    data class MediaFile(
        val name: String,
        val path: String,
        val date: Instant
    )
}