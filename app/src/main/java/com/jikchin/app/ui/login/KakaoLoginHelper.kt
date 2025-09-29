package com.jikchin.app.ui.login

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

/**
 * 사용법:
 *   launchKakaoLogin(
 *      context = this,
 *      onSuccess = { access -> vm.socialLogin("kakao", access) },
 *      onFailure = { e -> vm.fail(e.message ?: "Kakao 로그인 실패") }
 *   )
 */
fun launchKakaoLogin(
    context: Context,
    onSuccess: (String) -> Unit,
    onFailure: (Throwable) -> Unit
) {
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        when {
            error != null -> onFailure(error)
            token?.accessToken != null -> onSuccess(token.accessToken!!)
            else -> onFailure(IllegalStateException("Empty Kakao access token"))
        }
    }

    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
    } else {
        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
    }
}