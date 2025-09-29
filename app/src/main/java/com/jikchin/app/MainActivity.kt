package com.jikchin.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jikchin.app.data.remote.network.ServiceLocator
import com.jikchin.app.data.repository.AuthRepository
import com.jikchin.app.ui.login.LoginScreen
import com.jikchin.app.ui.login.LoginViewModel
import com.jikchin.app.ui.theme.JikchinandroidTheme
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import androidx.activity.result.contract.ActivityResultContracts
class MainActivity : ComponentActivity() {

    private lateinit var googleClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ServiceLocator, Naver init은 Application에서 이미 완료됨
        val authRepo = AuthRepository(ServiceLocator.authApi, ServiceLocator.tokenStore)
        val vm = LoginViewModel(authRepo)

        // Google init (여기는 OK)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()
        googleClient = GoogleSignIn.getClient(this, gso)

        val googleLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            runCatching {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
                val idToken = account.idToken ?: error("Empty Google ID token")
                idToken
            }.onSuccess { idToken ->
                vm.socialLogin("google", idToken)
            }.onFailure {
                vm.fail("Google 로그인 실패")
            }
        }

        setContent {
            JikchinandroidTheme {
                LoginScreen(
                    vm = vm,
                    onClickGoogle = { googleLauncher.launch(googleClient.signInIntent) },
                    onClickKakao = {
                        val tryAccount = {
                            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                                if (error != null) vm.fail("Kakao 로그인 실패")
                                else vm.socialLogin("kakao", token?.accessToken ?: return@loginWithKakaoAccount vm.fail("Kakao 토큰 비어있음"))
                            }
                        }
                        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                            if (error != null) tryAccount()
                            else vm.socialLogin("kakao", token?.accessToken ?: return@loginWithKakaoTalk vm.fail("Kakao 토큰 비어있음"))
                        }
                    },
                    onClickNaver = {
                        NaverIdLoginSDK.authenticate(this, object : OAuthLoginCallback {
                            override fun onSuccess() {
                                val t = NaverIdLoginSDK.getAccessToken()
                                if (t.isNullOrBlank()) vm.fail("Naver 토큰 비어있음")
                                else vm.socialLogin("naver", t)
                            }
                            override fun onFailure(httpStatus: Int, message: String) {
                                vm.fail("Naver 로그인 실패: $message")
                            }
                            override fun onError(errorCode: Int, message: String) {
                                vm.fail("Naver 로그인 에러: $message")
                            }
                        })
                    },
                    onNext = { needProfile ->
                        // TODO: 라우팅
                    }
                )
            }
        }
    }
}