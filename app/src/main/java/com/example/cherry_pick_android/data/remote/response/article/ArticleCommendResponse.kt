package com.example.cherry_pick_android.data.remote.response.article


import com.squareup.moshi.Json

data class ArticleCommendResponse(
    @field:Json(name = "data")
    val `data`: Data?,
    @field:Json(name = "description")
    val description: String?,
    @field:Json(name = "status")
    val status: String,
    @field:Json(name = "statusCode")
    val statusCode: Int,
    @field:Json(name = "transaction_time")
    val transactionTime: String?
) {
    data class Data(
        @field:Json(name = "content")
        val content: List<Content>,
        @field:Json(name = "empty")
        val empty: Boolean,
        @field:Json(name = "first")
        val first: Boolean,
        @field:Json(name = "last")
        val last: Boolean,
        @field:Json(name = "number")
        val number: Int,
        @field:Json(name = "numberOfElements")
        val numberOfElements: Int,
        @field:Json(name = "pageable")
        val pageable: Pageable,
        @field:Json(name = "size")
        val size: Int,
        @field:Json(name = "sort")
        val sort: Sort,
        @field:Json(name = "totalElements")
        val totalElements: Int,
        @field:Json(name = "totalPages")
        val totalPages: Int
    ) {
        data class Content(
            @field:Json(name = "articleId")
            val articleId: Int,
            @field:Json(name = "articlePhoto")
            val articlePhoto: List<ArticlePhoto>,
            @field:Json(name = "content")
            val content: String,
            @field:Json(name = "industry")
            val industry: String,
            @field:Json(name = "publisher")
            val publisher: String,
            @field:Json(name = "reporter")
            val reporter: String,
            @field:Json(name = "title")
            val title: String,
            @field:Json(name = "uploadedAt")
            val uploadedAt: String
        ) {
            data class ArticlePhoto(
                @field:Json(name = "articleImgUrl")
                val articleImgUrl: String,
                @field:Json(name = "imgDesc")
                val imgDesc: String
            )
        }

        data class Pageable(
            @field:Json(name = "offset")
            val offset: Int,
            @field:Json(name = "pageNumber")
            val pageNumber: Int,
            @field:Json(name = "pageSize")
            val pageSize: Int,
            @field:Json(name = "paged")
            val paged: Boolean,
            @field:Json(name = "sort")
            val sort: Sort,
            @field:Json(name = "unpaged")
            val unpaged: Boolean
        ) {
            data class Sort(
                @field:Json(name = "empty")
                val empty: Boolean,
                @field:Json(name = "sorted")
                val sorted: Boolean,
                @field:Json(name = "unsorted")
                val unsorted: Boolean
            )
        }

        data class Sort(
            @field:Json(name = "empty")
            val empty: Boolean,
            @field:Json(name = "sorted")
            val sorted: Boolean,
            @field:Json(name = "unsorted")
            val unsorted: Boolean
        )
    }
}