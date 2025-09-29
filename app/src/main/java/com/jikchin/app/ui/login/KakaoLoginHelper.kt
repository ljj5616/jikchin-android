package com.jikchin.app.ui.login

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class KakaoLoginHelper(private val context: Context) {
    private var onSuccess: ((String) -> Unit)? = null
    private var onFailure: ((Throwable) -> Unit)? = null

    fun setup(onSuccess: (String) -> Unit, onFailure: (Throwable) -> Unit) {
        this.onSuccess = onSuccess
        this.onFailure = onFailure
    }

    fun launch() {
        val cb: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            when {
                error != null -> onFailure?.invoke(error)
                token?.accessToken != null -> onSuccess?.invoke(token.accessToken!!)
                else -> onFailure?.invoke(IllegalStateException("Empty Kakao access token"))
            }
        }
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = cb)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = cb)
        }
    }
}