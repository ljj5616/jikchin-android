package com.jikchin.app.data.repository

import com.jikchin.app.data.remote.api.UserApi
import com.jikchin.app.domain.model.MeResponse

class MeRepository(
    private val userApi: UserApi
) {
    suspend fun getMe(): MeResponse = userApi.getMe()
}