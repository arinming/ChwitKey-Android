package com.example.cherry_pick_android.data.remote.response

data class ArticleIndustryResponse(
    val `data`: Data,
    val description: String,
    val status: String,
    val statusCode: Int,
    val transaction_time: String
) {
    data class Data(
        val content: List<Content>,
        val empty: Boolean,
        val first: Boolean,
        val last: Boolean,
        val number: Int,
        val numberOfElements: Int,
        val pageable: Pageable,
        val size: Int,
        val sort: Sort,
        val totalElements: Int,
        val totalPages: Int
    ) {
        data class Content(
            val articlePhoto: List<String>,
            val content: String,
            val publisher: String,
            val reporter: String,
            val title: String,
            val uploadedAt: String
        )

        data class Pageable(
            val offset: Int,
            val pageNumber: Int,
            val pageSize: Int,
            val paged: Boolean,
            val sort: Sort,
            val unpaged: Boolean
        ) {
            data class Sort(
                val empty: Boolean,
                val sorted: Boolean,
                val unsorted: Boolean
            )
        }

        data class Sort(
            val empty: Boolean,
            val sorted: Boolean,
            val unsorted: Boolean
        )
    }
}