package com.example.cherry_pick_android.data.remote.response.article

data class ArticleLikeResponse(
    val `data`: Data,
    val description: String,
    val status: String,
    val statusCode: Int,
    val transaction_time: String
) {
    class Data
}