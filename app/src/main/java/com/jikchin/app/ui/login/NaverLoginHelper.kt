package com.jikchin.app.ui.login

import android.app.Activity
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback

/**
 * 사용법:
 *   launchNaverLogin(
 *      activity = this,
 *      onSuccess = { access -> vm.socialLogin("naver", access) },
 *      onFailure = { e -> vm.fail(e.message ?: "Naver 로그인 실패") }
 *   )
 */
fun launchNaverLogin(
    activity: Activity,
    onSuccess: (String) -> Unit,
    onFailure: (Throwable) -> Unit
) {
    NaverIdLoginSDK.authenticate(activity, object : OAuthLoginCallback {
        override fun onSuccess() {
            val token = NaverIdLoginSDK.getAccessToken()
            if (token.isNullOrBlank()) {
                onFailure(IllegalStateException("Empty Naver access token"))
            } else {
                onSuccess(token)
            }
        }
        override fun onFailure(httpStatus: Int, message: String) {
            onFailure(IllegalStateException("Naver fail $httpStatus: $message"))
        }
        override fun onError(errorCode: Int, message: String) {
            onFailure(IllegalStateException("Naver error $errorCode: $message"))
        }
    })
}