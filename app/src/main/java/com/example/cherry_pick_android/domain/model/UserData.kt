package com.example.cherry_pick_android.domain.model

data class UserData(
    val userId: String,
    val platform: String,
    val token: String,
    val name: String,
    val gender: String,
    val birthday: String,
    val isInit: String
)
