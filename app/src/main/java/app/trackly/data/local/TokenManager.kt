package app.trackly.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "auth_data")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private companion object { val TOKEN_KEY = stringPreferencesKey("jwt_token") }

    val token = context.dataStore.data.map { it[TOKEN_KEY] }

    suspend fun getToken(): String? {
        return token.first()
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun clearToken() {
        context.dataStore.edit { it.remove(TOKEN_KEY) }
    }
}