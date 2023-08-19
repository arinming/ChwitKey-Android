package com.example.cherry_pick_android.data.remote.response.article


import com.squareup.moshi.Json

data class ArticleIndustryResponse(
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
        @Json(name = "content")
        val content: List<Content>,
        @Json(name = "empty")
        val empty: Boolean,
        @Json(name = "first")
        val first: Boolean,
        @Json(name = "last")
        val last: Boolean,
        @Json(name = "number")
        val number: Int,
        @Json(name = "numberOfElements")
        val numberOfElements: Int,
        @Json(name = "pageable")
        val pageable: Pageable,
        @Json(name = "size")
        val size: Int,
        @Json(name = "sort")
        val sort: Sort,
        @Json(name = "totalElements")
        val totalElements: Int,
        @Json(name = "totalPages")
        val totalPages: Int
    ) {
        data class Content(
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
            val url: String
        ) {
            data class ArticlePhoto(
                @Json(name = "articleImgUrl")
                val articleImgUrl: String,
                @Json(name = "imgDesc")
                val imgDesc: String
            )
        }

        data class Pageable(
            @Json(name = "offset")
            val offset: Int,
            @Json(name = "pageNumber")
            val pageNumber: Int,
            @Json(name = "pageSize")
            val pageSize: Int,
            @Json(name = "paged")
            val paged: Boolean,
            @Json(name = "sort")
            val sort: Sort,
            @Json(name = "unpaged")
            val unpaged: Boolean
        ) {
            data class Sort(
                @Json(name = "empty")
                val empty: Boolean,
                @Json(name = "sorted")
                val sorted: Boolean,
                @Json(name = "unsorted")
                val unsorted: Boolean
            )
        }

        data class Sort(
            @Json(name = "empty")
            val empty: Boolean,
            @Json(name = "sorted")
            val sorted: Boolean,
            @Json(name = "unsorted")
            val unsorted: Boolean
        )
    }
}