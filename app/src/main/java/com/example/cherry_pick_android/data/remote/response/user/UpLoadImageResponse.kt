package com.example.cherry_pick_android.data.remote.response.user


import com.squareup.moshi.Json

data class UpLoadImageResponse(
    @field:Json(name = "error")
    val error: String?,
    @field:Json(name = "path")
    val path: String?,
    @field:Json(name = "status")
    val status: Int?,
    @field:Json(name = "timestamp")
    val timestamp: String?
)