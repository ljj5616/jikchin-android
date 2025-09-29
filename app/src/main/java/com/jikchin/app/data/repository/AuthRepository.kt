package com.jikchin.app.data.repository

import com.jikchin.app.data.local.TokenStore
import com.jikchin.app.data.remote.api.AuthApi
import com.jikchin.app.domain.model.AccessTokenRequest
import com.jikchin.app.domain.model.CompleteProfileRequest

class AuthRepository(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore
) {
    data class LoginResult(val needProfile: Boolean)

    suspend fun socialLogin(provider: String, socialTokenOrIdToken: String): Result<LoginResult> =
        runCatching {
            val res = authApi.socialLogin(provider, AccessTokenRequest(socialTokenOrIdToken))
            tokenStore.save(res.accessToken, res.refreshToken)
            LoginResult(needProfile = res.needProfile)
        }

    suspend fun completeProfile(body: CompleteProfileRequest): Result<Unit> =
        runCatching {
            val res = authApi.completeProfile(body)
            tokenStore.save(res.accessToken, res.refreshToken)
        }
    suspend fun logout() = tokenStore.clear()
}