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
import com.jikchin.app.BuildConfig

class MainActivity : ComponentActivity() {

    private lateinit var googleClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrofit/TokenStore 초기화
        ServiceLocator.init(this)
        val authRepo = AuthRepository(ServiceLocator.authApi, ServiceLocator.tokenStore)
        val vm = LoginViewModel(authRepo)

        // Naver init
        NaverIdLoginSDK.initialize(
            applicationContext,
            BuildConfig.NAVER_CLIENT_ID,
            BuildConfig.NAVER_CLIENT_SECRET,
            BuildConfig.NAVER_APP_NAME
        )

        // Google init
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()
        googleClient = GoogleSignIn.getClient(this, gso)

        val googleLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            runCatching { task.result }
                .onSuccess { account ->
                    val idToken = account.idToken ?: ""
                    vm.socialLogin("google", idToken)
                }
                .onFailure {
                    vm.socialLogin("google", "")
                }
        }

        setContent {
            JikchinandroidTheme {
                LoginScreen(
                    vm = vm,
                    onClickGoogle = { googleLauncher.launch(googleClient.signInIntent) },
                    onClickKakao = {
                        val tryAccount = {
                            UserApiClient.instance.loginWithKakaoAccount(this) { token, _ ->
                                vm.socialLogin("kakao", token?.accessToken ?: "")
                            }
                        }
                        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                            if (error != null) tryAccount()
                            else vm.socialLogin("kakao", token?.accessToken ?: "")
                        }
                    },
                    onClickNaver = {
                        NaverIdLoginSDK.authenticate(this, object : OAuthLoginCallback {
                            override fun onSuccess() {
                                val t = NaverIdLoginSDK.getAccessToken() ?: ""
                                vm.socialLogin("naver", t)
                            }
                            override fun onFailure(httpStatus: Int, message: String) {
                                vm.socialLogin("naver", "")
                            }
                            override fun onError(errorCode: Int, message: String) {
                                vm.socialLogin("naver", "")
                            }
                        })
                    },
                    onNext = { needProfile ->
                        if (needProfile) {
                            // 온보딩 화면으로 이동
                        } else {
                            // 메인 화면으로 이동
                        }
                    }
                )
            }
        }
    }
}