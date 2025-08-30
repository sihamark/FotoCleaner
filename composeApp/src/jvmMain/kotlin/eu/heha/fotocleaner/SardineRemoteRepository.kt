package eu.heha.fotocleaner

import com.github.sardine.DavResource
import com.github.sardine.Sardine
import com.github.sardine.SardineFactory
import eu.heha.fotocleaner.model.CredentialsStore
import eu.heha.fotocleaner.model.RemoteRepository
import io.github.aakira.napier.Napier
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.ZoneOffset

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

    override suspend fun listImageFiles(): List<String> = withContext(Dispatchers.IO) {
        val url = credentialsStore.getCredentials()?.url
            ?: error("no credentials save, call loadImageFiles")
        val contentTypes = loadedResources.map { it.contentType }.distinct()
        Napier.d { "found content types: $contentTypes" }

        loadedResources.first {
            (it.isImageFile() || it.isVideoFile()) && !it.isPendingFile() && !it.isTrashedFile()
        }.let { resource ->
            Napier.d { resource.printableString() }
            val timestamp = resource.modified.toInstant()
            val dateTime = timestamp.atOffset(ZoneOffset.UTC)
            val resourceUrl = URLBuilder(url)
                .appendPathSegments(dateTime.year.toString(), dateTime.monthValue.toString())
                .buildString()
            val resources = sardine.list(resourceUrl.encodeURLPath())
            Napier.i { "found ${resources.size} resources" }
        }

        return@withContext listOf()
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
        appendLine("name: $name")
        appendLine("contentType: $contentType")
        appendLine("path: $path")
        appendLine("href: $href")
        appendLine("isDirectory: $isDirectory")
        appendLine("created: $creation")
        appendLine("modified: $modified")
        appendLine("etag: $etag")
        appendLine("size: $contentLength")
        appendLine("supported reports: $supportedReports")
    }
}