package com.example.cherry_pick_android.data.remote.response.user

import com.squareup.moshi.Json

data class UpdateNameResponse(
    @field:Json(name = "name")
    val name: String?
)
