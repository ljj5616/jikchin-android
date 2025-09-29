package com.jikchin.app.ui.login

import android.app.Activity
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback

class NaverLoginHelper(private val activity: Activity) {
    private var onSuccess: ((String) -> Unit)? = null
    private var onFailure: ((Throwable) -> Unit)? = null

    fun setup(onSuccess: (String) -> Unit, onFailure: (Throwable) -> Unit) {
        this.onSuccess = onSuccess
        this.onFailure = onFailure
    }

    fun launch() {
        NaverIdLoginSDK.authenticate(activity, object : OAuthLoginCallback {
            override fun onSuccess() {
                val t = NaverIdLoginSDK.getAccessToken()
                if (t.isNullOrBlank()) onFailure?.invoke(IllegalStateException("Empty Naver access token"))
                else onSuccess?.invoke(t)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                onFailure?.invoke(IllegalStateException("Naver fail $httpStatus: $message"))
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure?.invoke(IllegalStateException("Naver error $errorCode: $message"))
            }
        })
    }
}
