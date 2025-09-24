package com.jikchin.app.domain.model

class CompleteProfileRequest(
    val nickname: String,
    val avatarKey: String?,
    val bio: String?,
    val favoriteKboTeamId: Long?,
    val favoriteKleagueTeamId: Long?
)