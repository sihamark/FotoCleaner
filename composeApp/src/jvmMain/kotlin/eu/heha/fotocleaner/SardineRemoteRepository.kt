@file:OptIn(ExperimentalTime::class)

package eu.heha.fotocleaner

import com.github.sardine.DavResource
import com.github.sardine.Sardine
import com.github.sardine.SardineFactory
import com.github.sardine.impl.SardineException
import eu.heha.fotocleaner.model.CredentialsStore
import eu.heha.fotocleaner.model.RemoteRepository
import eu.heha.fotocleaner.model.RemoteRepository.AnalysisResult
import io.github.aakira.napier.Napier
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.ZoneOffset
import kotlin.time.ExperimentalTime
import kotlin.time.toKotlinInstant

class SardineRemoteRepository : RemoteRepository {

    private val credentialsStore = CredentialsStore()

    private lateinit var sardine: Sardine
    private lateinit var loadedResources: List<DavResource>

    override suspend fun loadFiles(
        url: String, userName: String, password: String
    ) {
        withContext(Dispatchers.IO) {
            sardine = SardineFactory.begin(userName, password)
            val resources = sardine.list(url.encodeURLPath())
            if (resources.isEmpty()) {
                throw Exception("No resources found at the provided URL")
            }
            Napier.e { "found ${resources.size} resources" }
            credentialsStore.saveCredentials(url, userName, password)
            loadedResources = resources
        }
    }

    override suspend fun listMediaFiles(): AnalysisResult = withContext(Dispatchers.IO) {
        val url = credentialsStore.getCredentials()?.url
            ?: error("no credentials save, call loadImageFiles")
        val contentTypes = loadedResources.map { it.contentType }.distinct()
        Napier.d { "found content types: $contentTypes" }

        val mediaFiles = loadedResources
            .sortedByDescending { it.modified }
            .filter {
                (it.isImageFile() || it.isVideoFile()) && !it.isPendingFile() && !it.isTrashedFile()
            }.map { resource ->
                RemoteRepository.MediaFile(
                    name = resource.name,
                    path = resource.path,
                    date = resource.modified.toInstant().toKotlinInstant()
                )
            }

        return@withContext AnalysisResult(
            directory = url,
            mediaFiles = mediaFiles
        )
    }

    private fun checkSubDirectory(
        resource: DavResource,
        url: String
    ) {
        val timestamp = resource.modified.toInstant()
        val dateTime = timestamp.atOffset(ZoneOffset.UTC)
        val resourceUrl = URLBuilder(url)
            .appendPathSegments(
                dateTime.year.toString(),
                "%02d".format(dateTime.monthValue)
            )
            .buildString()
        Napier.i { "resource ${resource.name} (modified: ${resource.modified}) should be in $resourceUrl" }
        try {
            val resources = sardine.list(resourceUrl.encodeURLPath())
            resources.first { it.name == resource.name }
                .also { Napier.i { "found it in $resourceUrl \n${it.printableString()}" } }
            Napier.i { "found ${resources.size} resources in $resourceUrl" }
        } catch (e: SardineException) {
            Napier.e(e) { "error loading $resourceUrl" }
        }
    }

    override suspend fun getStoredCredentials(): CredentialsStore.Credentials? =
        credentialsStore.getCredentials()

    private fun DavResource.isImageFile(): Boolean =
        contentType.substringBefore("/") == "image"

    private fun DavResource.isVideoFile(): Boolean =
        contentType.substringBefore("/") == "video"

    private fun DavResource.isPendingFile(): Boolean =
        name.startsWith(".pending")

    private fun DavResource.isTrashedFile(): Boolean =
        name.startsWith(".trashed")

    private fun DavResource.printableString() = buildString {
        appendLine("\tname: $name")
        appendLine("\tcontentType: $contentType")
        appendLine("\tpath: $path")
        appendLine("\thref: $href")
        appendLine("\tisDirectory: $isDirectory")
        appendLine("\tcreated: $creation")
        appendLine("\tmodified: $modified")
        appendLine("\tetag: $etag")
        appendLine("\tsize: $contentLength")
        appendLine("\tsupported reports: $supportedReports")
    }
}