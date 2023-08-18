package com.example.cherry_pick_android.data.remote.response.gpt


import com.squareup.moshi.Json

data class GptSelectResponse(
    @field:Json(name = "data")
    val `data`: SelectData?,
    @field:Json(name = "description")
    val description: Any?,
    @field:Json(name = "status")
    val status: String?,
    @field:Json(name = "statusCode")
    val statusCode: Int?,
    @field:Json(name = "transaction_time")
    val transactionTime: String?
)

data class SelectData(
    @field:Json(name = "answer")
    val answer: String?
)