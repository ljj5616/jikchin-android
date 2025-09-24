package com.jikchin.app.data.remote.network

import com.jikchin.app.data.local.TokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenStore: TokenStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val access = tokenStore.getAccessToken()
        val req = if (!access.isNullOrBlank()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $access")
                .build()
        } else chain.request()
        return chain.proceed(req)
    }
}