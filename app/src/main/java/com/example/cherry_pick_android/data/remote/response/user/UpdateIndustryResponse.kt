package com.example.cherry_pick_android.data.remote.response.user


import com.squareup.moshi.Json

data class UpdateIndustryResponse(
    @Json(name = "error")
    val error: String?,
    @Json(name = "path")
    val path: String?,
    @Json(name = "status")
    val status: Int?,
    @Json(name = "timestamp")
    val timestamp: String?
)