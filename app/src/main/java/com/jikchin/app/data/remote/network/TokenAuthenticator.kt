package com.jikchin.app.data.remote.network

import com.jikchin.app.data.local.TokenStore
import com.jikchin.app.data.remote.api.AuthApi
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenStore: TokenStore,
    private val authApi: AuthApi
): Authenticator  {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null
        val refresh = runBlocking { tokenStore.getRefresh() } ?: return null
        val bearer = if (refresh.startsWith("Bearer ")) refresh else "Bearer $refresh"

        val tokens = try {
            runBlocking { authApi.reissue(bearer) }
        } catch (_: Exception) {
            return null
        }
        runBlocking { tokenStore.save(tokens.accessToken, tokens.refreshToken) }

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${tokens.accessToken}")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var r: Response? = response
        var count = 1
        while (r?.priorResponse != null) {
            count++
            r = r.priorResponse
        }
        return count
    }
}