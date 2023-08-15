package com.example.cherry_pick_android.data.remote.response.user


import com.squareup.moshi.Json

data class UserKeywordResponse(
    @Json(name = "data")
    val `data`: Data?,
    @Json(name = "description")
    val description: Any?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "statusCode")
    val statusCode: Int?,
    @Json(name = "transaction_time")
    val transactionTime: String?
)