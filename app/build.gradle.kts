plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.jikchin.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jikchin.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/\"")
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"e4882b08806d1be78a7bcfd25f86d4e5\"")
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"256750487558-joj181fgtog89sq0qc33360s4bt6cuq0.apps.googleusercontent.com\"")
        buildConfigField("String", "NAVER_CLIENT_ID", "\"DQGp0HgGDLXK_JbDDKHf\"")
        buildConfigField("String", "NAVER_CLIENT_SECRET", "\"JZzTGIPMWs\"")
        buildConfigField("String", "NAVER_APP_NAME", "\"Jikchin\"")
    }

    buildTypes {
        debug {
            // 개발용: 난독화/최적화 끔
            isMinifyEnabled = false
        }
        release {
            // 배포용: 난독화/최적화 켬
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // 네트워킹/저장
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Kakao Login SDK
    implementation ("com.kakao.sdk:v2-all:2.21.7") // 전체 모듈 설치, 2.11.0 버전부터 지원

    // Naver Login SDK
    implementation("com.navercorp.nid:oauth:5.9.1") // (AndroidX)
}