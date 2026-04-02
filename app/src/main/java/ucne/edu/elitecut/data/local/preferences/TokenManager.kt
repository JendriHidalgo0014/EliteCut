package ucne.edu.elitecut.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "elite_cut_prefs")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_CORREO_KEY = stringPreferencesKey("user_correo")
        private val USER_TELEFONO_KEY = stringPreferencesKey("user_telefono")
        private val USER_FECHA_INGRESO_KEY = stringPreferencesKey("user_fecha_ingreso")
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
    val userRoleFlow: Flow<String?> = context.dataStore.data.map { it[USER_ROLE_KEY] }
    val userIdFlow: Flow<Int?> = context.dataStore.data.map { it[USER_ID_KEY] }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun saveUserInfo(
        id: Int,
        nombre: String,
        correo: String,
        telefono: String,
        fechaIngreso: String,
        rol: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID_KEY] = id
            prefs[USER_NAME_KEY] = nombre
            prefs[USER_CORREO_KEY] = correo
            prefs[USER_TELEFONO_KEY] = telefono
            prefs[USER_FECHA_INGRESO_KEY] = fechaIngreso
            prefs[USER_ROLE_KEY] = rol
        }
    }

    suspend fun getToken(): String? = context.dataStore.data.first()[TOKEN_KEY]
    suspend fun getUserId(): Int? = context.dataStore.data.first()[USER_ID_KEY]
    suspend fun getUserRole(): String? = context.dataStore.data.first()[USER_ROLE_KEY]
    suspend fun getUserName(): String? = context.dataStore.data.first()[USER_NAME_KEY]
    suspend fun getUserCorreo(): String? = context.dataStore.data.first()[USER_CORREO_KEY]
    suspend fun getUserTelefono(): String? = context.dataStore.data.first()[USER_TELEFONO_KEY]
    suspend fun getUserFechaIngreso(): String? = context.dataStore.data.first()[USER_FECHA_INGRESO_KEY]

    suspend fun isLoggedIn(): Boolean = getToken() != null

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}