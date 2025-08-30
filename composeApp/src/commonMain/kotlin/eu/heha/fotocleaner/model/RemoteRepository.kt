package eu.heha.fotocleaner.model

interface RemoteRepository {

    suspend fun login(url: String, userName: String, password: String)

    suspend fun getStoredCredentials(): CredentialsStore.Credentials?
}