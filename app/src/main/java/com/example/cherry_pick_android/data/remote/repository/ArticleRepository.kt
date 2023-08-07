package com.example.cherry_pick_android.data.remote.repository

import com.example.cherry_pick_android.data.data.Pageable
import com.example.cherry_pick_android.data.remote.request.RetrofitClient
import com.example.cherry_pick_android.data.remote.service.ArticleDetailService
import com.example.cherry_pick_android.data.remote.service.ArticleSearchService

class ArticleRepository {
    private val client = RetrofitClient.getInstance().create(ArticleSearchService::class.java)
    suspend fun getAllArticle() = client.getArticleCommend("", "", Pageable)
}