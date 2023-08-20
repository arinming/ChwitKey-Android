package com.example.cherry_pick_android.data.remote.service.article

import com.example.cherry_pick_android.data.remote.response.article.ArticleIndustryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleSearchIndustryService {
    // 직군 & 정렬
    @GET("/api/articles/search/industry")
    suspend fun getArticleIndustry(
        @Query("sortType") sortType: String,
        @Query("industry") industry: String,
        @Query("page") page: Int
    ): Response<ArticleIndustryResponse>
}