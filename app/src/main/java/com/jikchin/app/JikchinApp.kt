package com.jikchin.app

import com.kakao.sdk.common.KakaoSdk
import android.app.Application
import com.jikchin.app.data.remote.network.ServiceLocator
import com.navercorp.nid.NaverIdLoginSDK

class JikchinApp : Application() {
    override fun onCreate() {
        super.onCreate()

        ServiceLocator.init(this)

        // KAKAO
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)

        // NAVER
        NaverIdLoginSDK.initialize(
            this,
            BuildConfig.NAVER_CLIENT_ID,
            BuildConfig.NAVER_CLIENT_SECRET,
            BuildConfig.NAVER_APP_NAME
        )
    }
}