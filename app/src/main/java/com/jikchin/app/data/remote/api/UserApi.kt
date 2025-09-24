package com.jikchin.app.data.remote.api

import com.jikchin.app.domain.model.MeResponse
import retrofit2.http.GET

interface UserApi {
    @GET("users/me")
    suspend fun getMe(): MeResponse
}