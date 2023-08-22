package com.chwitkey.cherry_pick_android.data.remote.response.article


import com.squareup.moshi.Json

data class ArticleLikeResponse(
    @field:Json(name = "data")
    val `data`: Any?,
    @field:Json(name = "description")
    val description: String?,
    @field:Json(name = "status")
    val status: String,
    @field:Json(name = "statusCode")
    val statusCode: Int?,
    @field:Json(name = "transaction_time")
    val transactionTime: String?
)