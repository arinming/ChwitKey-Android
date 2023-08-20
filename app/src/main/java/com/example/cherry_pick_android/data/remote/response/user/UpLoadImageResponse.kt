package com.example.cherry_pick_android.data.remote.response.user


import com.example.cherry_pick_android.data.remote.response.article.ArticleDetailResponse
import com.squareup.moshi.Json

data class UpLoadImageResponse(
    @Json(name = "data")
    val data: String,
    @Json(name = "transaction_time")
    val transactionTime: String?,
    @Json(name = "status")
    val status: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "statusCode")
    val statusCode: Int
)