package com.example.cherry_pick_android.data.remote.service.article

import com.example.cherry_pick_android.data.data.Pageable
import com.example.cherry_pick_android.data.remote.response.article.ArticleKeywordResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleSearchKeywordService {
    // 키워드 & 정렬
    @GET("/api/articles/search/keyword")
    suspend fun getArticleKeyword(
        @Query("memberId") memberId: Int,
        @Query("sortType") sortType: String,
        @Query("industry") industry: String,
        @Query("pageable") pageable: Pageable
    ): Response<ArticleKeywordResponse>
}