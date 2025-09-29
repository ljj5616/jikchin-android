package com.jikchin.app.data.remote.network

import android.content.Context
import com.jikchin.app.BuildConfig
import com.jikchin.app.data.local.TokenStore
import com.jikchin.app.data.remote.api.AuthApi
import com.jikchin.app.data.remote.api.UserApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ServiceLocator {
    private lateinit var retrofit: Retrofit
    lateinit var authApi: AuthApi
        private set
    lateinit var userApi: UserApi
        private set
    lateinit var tokenStore: TokenStore
        private set

    fun init(context: Context, baseUrl: String = BuildConfig.BASE_URL) {
        val appCtx = context.applicationContext
        tokenStore = TokenStore(appCtx)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(tokenStore))
            .addInterceptor(logging)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        authApi = retrofit.create(AuthApi::class.java)
        userApi = retrofit.create(UserApi::class.java)
    }
}