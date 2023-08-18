package com.example.cherry_pick_android.data.remote.service.article

import com.example.cherry_pick_android.data.remote.response.article.ArticleLikeResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

// 관심 기사 요청
interface ArticleLikeService {
    @POST("/api/articles/like/{articleId}")
    suspend fun postArticleLike(
        @Query("articleId") articleId: Int,
        @Query("type") type: String
    ): Response<ArticleLikeResponse>
}