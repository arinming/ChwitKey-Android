package com.example.cherry_pick_android.data.remote.service

import com.example.cherry_pick_android.data.data.Pageable
import com.example.cherry_pick_android.data.remote.response.ArticleCommendResponse
import com.example.cherry_pick_android.data.remote.response.ArticleIndustryResponse
import com.example.cherry_pick_android.data.remote.response.ArticleKeywordResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// 기사 검색 요청
interface ArticleSearchService {
    // 커맨드 & 정렬
    @GET("/api/articles/search")
    suspend fun getArticleCommend(
        @Query("cond") cond: String,
        @Query("sortType") sortType: String,
        @Query("pageable") pageable: Pageable
    ): Response<ArticleCommendResponse>


    // 직군 & 정렬
    @GET("/api/articles/search/industry")
    suspend fun getArticleIndustry(
        @Query("memberId") memberId: Int,
        @Query("sortType") sortType: String,
        @Query("industry") industry: String,
        @Query("pageable") pageable: Pageable
    ): Response<ArticleIndustryResponse>


    // 키워드 & 정렬
    @GET("/api/articles/search/keyword")
    suspend fun getArticleKeyword(
        @Query("memberId") memberId: Int,
        @Query("sortType") sortType: String,
        @Query("industry") industry: String,
        @Query("pageable") pageable: Pageable
    ): Response<ArticleKeywordResponse>
}