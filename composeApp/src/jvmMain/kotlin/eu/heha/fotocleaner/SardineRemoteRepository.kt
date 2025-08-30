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
            resources.forEach { resource ->
                Napier.i { "resource: ${resource.name} path: ${resource.path} types: ${resource.resourceTypes.joinToString()}" }
            }
            credentialsStore.saveCredentials(url, userName, password)
        }
    }

    override suspend fun getStoredCredentials(): CredentialsStore.Credentials? =
        credentialsStore.getCredentials()
}