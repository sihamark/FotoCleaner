package eu.heha.fotocleaner.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import eu.heha.fotocleaner.FotoCleanerApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.io.files.Path
import okio.Path.Companion.toPath

class CredentialsStore {

    private val dataStore = create()

    suspend fun saveCredentials(url: String, userName: String, password: String) {
        dataStore.edit { prefs ->
            prefs[urlKey] = url
            prefs[userNameKey] = userName
            prefs[passwordKey] = password
        }
    }

    suspend fun getCredentials(): Credentials? = withContext(Dispatchers.Default) {
        val prefs = dataStore.data.first()
        val url = prefs[urlKey] ?: return@withContext null
        val userName = prefs[userNameKey] ?: return@withContext null
        val password = prefs[passwordKey] ?: return@withContext null
        return@withContext Credentials(url, userName, password)
    }

    suspend fun clearCredentials() {
        withContext(Dispatchers.Default) {
            dataStore.edit { it.clear() }
        }
    }

    data class Credentials(
        val url: String,
        val userName: String,
        val password: String
    )

    companion object {

        private val urlKey = stringPreferencesKey("login_url")
        private val userNameKey = stringPreferencesKey("login_user_name")
        private val passwordKey = stringPreferencesKey("login_password")

        private fun create(): DataStore<Preferences> =
            PreferenceDataStoreFactory.createWithPath(
                produceFile = {
                    Path(
                        FotoCleanerApp.filesRoot,
                        "credentials.preferences_pb"
                    ).toString().toPath()
                }
            )
    }
}