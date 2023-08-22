package com.chwitkey.cherry_pick_android.data.remote.request.login


import com.squareup.moshi.Json

data class SignInRequest(
    @field:Json(name = "memberNumber")
    val memberNumber: String?,
    @field:Json(name = "provider")
    val provider: String?
)