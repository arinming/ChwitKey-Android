package com.example.cherry_pick_android.data.remote.response.article


import com.squareup.moshi.Json

data class ArticleUnlikeResponse(
    @Json(name = "data")
    val `data`: Any?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "status")
    val status: String,
    @Json(name = "statusCode")
    val statusCode: Int?,
    @Json(name = "transaction_time")
    val transactionTime: String?
)