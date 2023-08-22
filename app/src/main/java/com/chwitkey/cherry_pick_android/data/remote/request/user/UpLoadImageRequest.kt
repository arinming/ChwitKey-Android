package com.chwitkey.cherry_pick_android.data.remote.request.user


import com.squareup.moshi.Json

data class UpLoadImageRequest(
    @field:Json(name = "image")
    val image: String?
)