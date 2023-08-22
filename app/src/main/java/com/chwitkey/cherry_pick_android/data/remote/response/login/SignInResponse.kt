package com.chwitkey.cherry_pick_android.data.remote.response.login


import com.squareup.moshi.Json

data class SignInResponse(
    @field:Json(name = "data")
    val `data`: Data?,
    @field:Json(name = "description")
    val description: String?,
    @field:Json(name = "status")
    val status: String?,
    @field:Json(name = "statusCode")
    val statusCode: Int?,
    @field:Json(name = "transaction_time")
    val transactionTime: String?
)

data class Data(
    @field:Json(name = "access_token")
    val access_token: String?,
    @field:Json(name = "isMember")
    val isMember: Boolean?,
    @field:Json(name = "refresh_token")
    val refresh_token: String?
)