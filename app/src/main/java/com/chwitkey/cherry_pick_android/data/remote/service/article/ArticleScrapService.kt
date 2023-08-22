package com.chwitkey.cherry_pick_android.data.remote.service.article

import com.chwitkey.cherry_pick_android.data.remote.response.article.ArticleScrapResponse
import retrofit2.Response
import retrofit2.http.GET

interface ArticleScrapService {
    @GET("/api/articles/scrap")
    suspend fun getScrap(): Response<ArticleScrapResponse>
}