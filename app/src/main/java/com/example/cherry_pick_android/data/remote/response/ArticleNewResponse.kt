package com.example.cherry_pick_android.data.remote.response

data class ArticleNewResponse(
    val `data`: Int,
    val description: String,
    val status: String,
    val statusCode: Int,
    val transaction_time: String
)