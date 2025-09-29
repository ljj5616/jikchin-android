package com.jikchin.app.domain.model

data class SocialLoginResponse(
    val accessToken: String?,
    val refreshToken: String?,
    val needProfile: Boolean
)