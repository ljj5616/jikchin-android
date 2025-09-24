package com.jikchin.app.data.remote.api

import com.jikchin.app.domain.model.AccessTokenRequest
import com.jikchin.app.domain.model.CompleteProfileRequest
import com.jikchin.app.domain.model.LoginRequest
import com.jikchin.app.domain.model.SocialLoginResponse
import com.jikchin.app.domain.model.TokenResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {
    @POST("auth/{provider}/login")
    suspend fun socialLogin(
        @Path("provider") provider: String,
        @Body body: AccessTokenRequest
    ): SocialLoginResponse

    @POST("auth/complete-profile")
    suspend fun completeProfile(
        @Body body: CompleteProfileRequest
    ): SocialLoginResponse
}