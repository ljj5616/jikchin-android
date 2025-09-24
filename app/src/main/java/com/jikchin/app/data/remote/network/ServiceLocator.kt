package com.jikchin.app.data.remote.network

import android.content.Context
import com.jikchin.app.data.local.TokenStore
import com.jikchin.app.data.remote.api.AuthApi
import com.jikchin.app.data.remote.api.UserApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ServiceLocator {
    private fun okHttp(tokenStore: TokenStore, authApi: AuthApi): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenStore))
            .authenticator(TokenAuthenticator(tokenStore, authApi))
            .addInterceptor(logging) // 디버그에서만 쓰는 걸 추천
            .build()
    }

    private fun retrofit(baseUrl: String, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    /**
     * 사용 예:
     * val (authApi, userApi, tokenStore) = ServiceLocator.init(BuildConfig.BASE_URL, context)
     */
    fun init(baseUrl: String, context: Context)
            : Triple<AuthApi, UserApi, TokenStore> {
        val tokenStore = TokenStore(context)

        // reissue 호출용 선(先) Retrofit
        val bootstrapRetrofit = retrofit(baseUrl, OkHttpClient())
        val bootstrapAuth = bootstrapRetrofit.create(AuthApi::class.java)

        val client = okHttp(tokenStore, bootstrapAuth)
        val mainRetrofit = retrofit(baseUrl, client)
        val authApi = mainRetrofit.create(AuthApi::class.java)
        val userApi = mainRetrofit.create(UserApi::class.java)
        return Triple(authApi, userApi, tokenStore)
    }
}