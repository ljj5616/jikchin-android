package com.jikchin.app.data.remote.api

import com.jikchin.app.domain.model.LoginRequest
import com.jikchin.app.domain.model.TokenResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): TokenResponse

    @POST("auth/reissue")
    suspend fun reissue(@Header("Authorization") refreshToken: String): TokenResponse
}