package com.chwitkey.cherry_pick_android.data.remote.request

data class ArticleNewDTO(
    val articlePhotos: List<ArticlePhoto>,
    val content: String,
    val industry: String,
    val publisher: String,
    val reporter: String,
    val title: String,
    val uploadedAt: String
) {
    data class ArticlePhoto(
        val articleImgUrl: String
    )
}