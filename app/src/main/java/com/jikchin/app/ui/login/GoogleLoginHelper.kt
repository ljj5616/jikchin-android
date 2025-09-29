package com.jikchin.app.ui.login

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.jikchin.app.BuildConfig

/**
 * 사용법:
 *   val google = GoogleLoginHelper(this).apply {
 *       setup(onSuccess = { idToken -> vm.socialLogin("google", idToken) },
 *             onFailure = { e -> vm.fail(e.message ?: "Google 로그인 실패") })
 *   }
 *   google.launch()
 *
 * strings.xml에 @string/google_web_client_id (웹 클라이언트 ID) 필요.
 */
class GoogleLoginHelper(private val activity: ComponentActivity) {
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var onSuccess: ((String) -> Unit)? = null
    private var onFailure: ((Throwable) -> Unit)? = null

    fun setup(onSuccess: (String) -> Unit, onFailure: (Throwable) -> Unit) {
        this.onSuccess = onSuccess
        this.onFailure = onFailure
        launcher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                if (idToken.isNullOrBlank()) {
                    onFailure(IllegalStateException("Empty Google ID token"))
                } else {
                    onSuccess(idToken)
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun launch() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(activity, gso)
        launcher.launch(client.signInIntent)
    }
}