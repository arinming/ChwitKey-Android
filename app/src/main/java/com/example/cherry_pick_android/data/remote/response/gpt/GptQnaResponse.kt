package com.example.cherry_pick_android.data.remote.response.gpt


import com.squareup.moshi.Json

data class GptQnaResponse(
    @field:Json(name = "data")
    val `data`: GPTQnAData?,
    @field:Json(name = "description")
    val description: String?,
    @field:Json(name = "status")
    val status: String?,
    @field:Json(name = "statusCode")
    val statusCode: Int?,
    @field:Json(name = "transaction_time")
    val transactionTime: String?
)

data class GPTQnAData(
    @field:Json(name = "answer")
    val answer: String?
)