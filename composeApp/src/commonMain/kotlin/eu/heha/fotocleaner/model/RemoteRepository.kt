@file:OptIn(ExperimentalTime::class)

package eu.heha.fotocleaner.model

import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface RemoteRepository {

    suspend fun loadFiles(url: String, userName: String, password: String)

    suspend fun getStoredCredentials(): CredentialsStore.Credentials?

    suspend fun listMediaFiles(): Flow<AnalysisResult>

    data class AnalysisResult(
        val directory: String,
        val progress: Progress,
        val mediaFiles: List<MediaFile>,
    )

    data class Progress(
        val current: Int,
        val total: Int,
        val startTime: Instant
    )

    data class MediaFile(
        val name: String,
        val path: String,
        val date: Instant,
        val subPath: String,
        val doesSubPathExist: Boolean? = null
    )
}