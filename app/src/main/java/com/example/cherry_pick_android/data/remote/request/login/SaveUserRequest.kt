package com.example.cherry_pick_android.data.remote.request.login


import com.squareup.moshi.Json

data class SaveUserRequest(
    @Json(name = "memberNumber")
    val memberNumber: String?,
    @Json(name = "provider")
    val provider: String?
)