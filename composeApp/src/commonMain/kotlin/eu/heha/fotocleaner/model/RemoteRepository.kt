package eu.heha.fotocleaner.model

interface RemoteRepository {

    suspend fun loadFiles(url: String, userName: String, password: String)

    suspend fun getStoredCredentials(): CredentialsStore.Credentials?

    suspend fun listImageFiles(): List<String>

}