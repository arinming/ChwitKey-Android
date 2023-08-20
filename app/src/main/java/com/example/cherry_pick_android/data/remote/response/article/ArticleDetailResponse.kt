package com.example.cherry_pick_android.data.remote.response.article


import com.squareup.moshi.Json

data class ArticleDetailResponse(
    @Json(name = "data")
    val `data`: Data?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "status")
    val status: String,
    @Json(name = "statusCode")
    val statusCode: Int,
    @Json(name = "transaction_time")
    val transactionTime: String?
) {
    data class Data(
        @Json(name = "articleId")
        val articleId: Int,
        @Json(name = "articlePhoto")
        val articlePhoto: List<ArticlePhoto>,
        @Json(name = "content")
        val content: String,
        @Json(name = "industry")
        val industry: String,
        @Json(name = "publisher")
        val publisher: String,
        @Json(name = "reporter")
        val reporter: String,
        @Json(name = "title")
        val title: String,
        @Json(name = "uploadedAt")
        val uploadedAt: String,
        @Json(name = "url")
        val url: String,
        @Json(name = "isLiked")
        val isLiked: Boolean,
        @Json(name = "isScrapped")
        val isScrapped: Boolean
    ) {
        data class ArticlePhoto(
            @Json(name = "articleImgUrl")
            val articleImgUrl: String,
            @Json(name = "imgDesc")
            val imgDesc: String
        )
    }
}