import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

// ----- helpers: plugins 아래로 배치 -----
val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}

fun secret(name: String, default: String? = null): String {
    val fromEnv = providers.environmentVariable(name).orNull
    if (!fromEnv.isNullOrBlank()) return fromEnv

    val fromGradleProp = providers.gradleProperty(name).orNull
    if (!fromGradleProp.isNullOrBlank()) return fromGradleProp

    val fromLocal = localProps.getProperty(name)
    if (!fromLocal.isNullOrBlank()) return fromLocal

    return default ?: throw GradleException("Missing secret: $name")
}

fun q(s: String) = "\"$s\"" // buildConfigField용 따옴표

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

        buildConfigField("String", "BASE_URL", q(secret("BASE_URL", "http://10.0.2.2:8080/")))
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", q(secret("KAKAO_NATIVE_APP_KEY")))
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", q(secret("GOOGLE_WEB_CLIENT_ID")))
        buildConfigField("String", "NAVER_CLIENT_ID", q(secret("NAVER_CLIENT_ID")))
        buildConfigField("String", "NAVER_CLIENT_SECRET", q(secret("NAVER_CLIENT_SECRET")))
        buildConfigField("String", "NAVER_APP_NAME", q(secret("NAVER_APP_NAME", "Jikchin")))
    }

    buildTypes {
        debug { isMinifyEnabled = false }
        release {
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
    kotlinOptions { jvmTarget = "17" }
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
    implementation("com.squareup.retrofit2:retrofit:2.12.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.12.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    debugImplementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.4.0")

    // Kakao Login SDK
    implementation("com.kakao.sdk:v2-all:2.21.7")

    // Naver Login SDK
    implementation("com.navercorp.nid:oauth:5.10.0")
}