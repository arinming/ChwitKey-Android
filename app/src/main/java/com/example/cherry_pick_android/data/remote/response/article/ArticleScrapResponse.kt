package com.example.cherry_pick_android.data.remote.response.article


import com.squareup.moshi.Json

data class ArticleScrapResponse(
    @Json(name = "data")
    val `data`: List<ScrapData?>?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "statusCode")
    val statusCode: Int?,
    @Json(name = "transaction_time")
    val transactionTime: String?
)

data class ScrapData(
    @Json(name = "articleId")
    val articleId: Int?,
    @Json(name = "publisher")
    val publisher: String?,
    @Json(name = "thumbnailUrl")
    val thumbnailUrl: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "uploadedAt")
    val uploadedAt: String?
)