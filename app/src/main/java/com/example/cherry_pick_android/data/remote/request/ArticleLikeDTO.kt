package com.example.cherry_pick_android.data.remote.request

data class ArticleLikeDTO(
    val articleId: Int,
    val memberId: Int,
    val type: String
)