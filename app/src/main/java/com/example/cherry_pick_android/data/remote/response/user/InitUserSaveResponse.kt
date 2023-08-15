package com.example.cherry_pick_android.data.remote.response.user


import com.squareup.moshi.Json

data class InitUserSaveResponse(
    @field:Json(name = "description")
    val description: Any?,
    @field:Json(name = "status")
    val status: String?,
    @field:Json(name = "statusCode")
    val statusCode: Int?,
    @field:Json(name = "transaction_time")
    val transactionTime: String?
)