package eu.heha.fotocleaner

import com.github.sardine.SardineFactory
import eu.heha.fotocleaner.model.CredentialsStore
import eu.heha.fotocleaner.model.RemoteRepository
import io.github.aakira.napier.Napier
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SardineRemoteRepository : RemoteRepository {

    private val credentialsStore = CredentialsStore()

    override suspend fun login(
        url: String, userName: String, password: String
    ) {
        withContext(Dispatchers.IO) {
            val sardine = SardineFactory.begin(userName, password)
            val resources = sardine.list(url.encodeURLPath())
            if (resources.isEmpty()) {
                throw Exception("No resources found at the provided URL")
            }
            Napier.e { "found ${resources.size} resources" }
            credentialsStore.saveCredentials(url, userName, password)
        }
    }

    override suspend fun listImageFiles(): List<String> = withContext(Dispatchers.IO) {
        val credentials = credentialsStore.getCredentials()
            ?: error("No stored credentials found")
        val sardine = SardineFactory.begin(credentials.userName, credentials.password)
        val resources = sardine.list(credentials.url.encodeURLPath())

        val contentTypes = resources.map { it.contentType }.distinct()
        Napier.d { "found content types: $contentTypes" }

        resources.filter { it.contentType.substringBefore("/") in setOf("image", "video") }
            .filterNot { it.name.startsWith(".pending") || it.name.startsWith(".trashed") }
            .first().let { resource ->
                Napier.d {
                    buildString {
                        appendLine("name: ${resource.name}")
                        appendLine("contentType: ${resource.contentType}")
                        appendLine("path: ${resource.path}")
                        appendLine("href: ${resource.href}")
                        appendLine("isDirectory: ${resource.isDirectory}")
                        appendLine("created: ${resource.creation}")
                        appendLine("modified: ${resource.modified}")
                        appendLine("size: ${resource.customProps}")
                        appendLine("size: ${resource.customPropsNS}")
                        appendLine("size: ${resource.resourceTypes}")
                        appendLine("size: ${resource.statusCode}")
                        appendLine("size: ${resource.supportedReports}")
                    }
                }
            }

        return@withContext listOf()
    }

    override suspend fun getStoredCredentials(): CredentialsStore.Credentials? =
        credentialsStore.getCredentials()
}