package ucne.edu.elitecut.data.remote

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ucne.edu.elitecut.data.local.preferences.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // No agregar token a endpoints de auth
        val path = originalRequest.url.encodedPath
        if (path.contains("auth/login") || path.contains("auth/register")) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking { tokenManager.getToken() }

        val request = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .build()
        } else {
            originalRequest.newBuilder()
                .header("Content-Type", "application/json")
                .build()
        }

        return chain.proceed(request)
    }
}
