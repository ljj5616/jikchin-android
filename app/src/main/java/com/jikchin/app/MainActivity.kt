package com.jikchin.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jikchin.app.data.remote.network.ServiceLocator
import com.jikchin.app.data.repository.AuthRepository
import com.jikchin.app.ui.login.GoogleLoginHelper
import com.jikchin.app.ui.login.KakaoLoginHelper
import com.jikchin.app.ui.login.NaverLoginHelper
import com.jikchin.app.ui.login.LoginScreen
import com.jikchin.app.ui.login.LoginViewModel
import com.jikchin.app.ui.theme.JikchinandroidTheme

class MainActivity : ComponentActivity() {
    private lateinit var vm: LoginViewModel
    private lateinit var google: GoogleLoginHelper
    private lateinit var kakao: KakaoLoginHelper
    private lateinit var naver: NaverLoginHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authRepo = AuthRepository(ServiceLocator.authApi, ServiceLocator.tokenStore)
        vm = LoginViewModel(authRepo)

        google = GoogleLoginHelper(this).apply {
            setup(
                onSuccess = { idToken -> vm.socialLogin("google", idToken) },
                onFailure = { e -> vm.fail(e.message ?: "Google 로그인 실패") }
            )
        }
        kakao = KakaoLoginHelper(this).apply {
            setup(
                onSuccess = { access -> vm.socialLogin("kakao", access) },
                onFailure = { e -> vm.fail(e.message ?: "Kakao 로그인 실패") }
            )
        }
        naver = NaverLoginHelper(this).apply {
            setup(
                onSuccess = { access -> vm.socialLogin("naver", access) },
                onFailure = { e -> vm.fail(e.message ?: "Naver 로그인 실패") }
            )
        }

        setContent {
            JikchinandroidTheme {
                LoginScreen(
                    vm = vm,
                    onClickGoogle = { google.launch() },
                    onClickKakao  = { kakao.launch() },
                    onClickNaver  = { naver.launch() },
                    onNext = { needProfile ->
                        // TODO: 라우팅
                    }
                )
            }
        }
    }
}