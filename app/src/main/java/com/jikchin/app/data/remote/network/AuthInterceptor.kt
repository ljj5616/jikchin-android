package com.jikchin.app.data.remote.network

import com.jikchin.app.data.local.TokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenStore: TokenStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val access = runBlocking { tokenStore.getAccess() }
        val newReq = if (!access.isNullOrBlank()) {
            req.newBuilder()
                .addHeader("Authorization", "Bearer $access")
                .build()
        } else req
        return chain.proceed(newReq)
    }
}