package com.chwitkey.cherry_pick_android.data.remote.response.gpt


import com.squareup.moshi.Json

data class NewGptResponse(
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
    @field:Json(name = "chatId")
    val chatId: Int?,
    @field:Json(name = "greeting")
    val greeting: String?
)