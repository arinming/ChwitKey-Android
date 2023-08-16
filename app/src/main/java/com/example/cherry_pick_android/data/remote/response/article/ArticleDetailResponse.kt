package com.example.cherry_pick_android.data.remote.response.article

data class ArticleDetailResponse(
    val `data`: Data,
    val description: String,
    val status: String,
    val statusCode: Int,
    val transaction_time: String
) {
    data class Data(
        val articlePhoto: List<ArticlePhoto>,
        val content: String,
        val publisher: String,
        val reporter: String,
        val title: String,
        val uploadedAt: String
    ) {
        data class ArticlePhoto(
            val articleImgUrl: String
        )
    }
}