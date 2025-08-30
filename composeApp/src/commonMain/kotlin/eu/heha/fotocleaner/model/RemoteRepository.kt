package eu.heha.fotocleaner.model

interface RemoteRepository {

    suspend fun login(url: String, userName: String, password: String)

    suspend fun getStoredCredentials(): CredentialsStore.Credentials?

    suspend fun listImageFiles(): List<String>

    data class MediaResource(
        val name: String,
        val contentType: String,
        val href: String,
        val modified: String
    )
}