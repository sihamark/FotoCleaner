package eu.heha.fotocleaner

import com.github.sardine.SardineFactory

class SardineRemoteRepository: RemoteRepository {
    override suspend fun login(
        url: String, userName: String, password: String
    ) {
        val sardine = SardineFactory.begin(userName, password)
        sardine.list(url)

    }
}