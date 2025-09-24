package com.jikchin.app

import com.kakao.sdk.common.KakaoSdk
import android.app.Application

class JikchinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}