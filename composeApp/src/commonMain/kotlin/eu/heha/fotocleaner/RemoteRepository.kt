package eu.heha.fotocleaner

interface RemoteRepository {
    suspend fun login(url: String, userName: String, password: String)
}