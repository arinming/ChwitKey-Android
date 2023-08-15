package com.example.cherry_pick_android.data.remote.response.login


import com.squareup.moshi.Json

data class SaveUserResponse(
    @Json(name = "data")
    val `data`: Data?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "statusCode")
    val statusCode: Int?,
    @Json(name = "transaction_time")
    val transactionTime: String?
)

data class Data(
    @Json(name = "access_token")
    val accessToken: String?,
    @Json(name = "isMember")
    val isMember: Boolean?,
    @Json(name = "refresh_token")
    val refreshToken: String?
)