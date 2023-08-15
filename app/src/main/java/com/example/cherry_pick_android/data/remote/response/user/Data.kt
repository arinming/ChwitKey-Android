package com.example.cherry_pick_android.data.remote.response.user


import com.squareup.moshi.Json

data class Data(
    @Json(name = "keywordList")
    val keywordList: List<Any?>?
)