package com.chwitkey.cherry_pick_android.data.remote.request.user


import com.squareup.moshi.Json

data class updateNameRequest(
    @field:Json(name = "updateName")
    val updateName: String?
)