package com.example.cherry_pick_android.data.remote.service.article

import com.example.cherry_pick_android.data.remote.request.ArticleLikeDTO
import com.example.cherry_pick_android.data.remote.response.article.ArticleLikeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// 관심 기사 요청
interface ArticleLikeService {
    @POST("/api/articles/like/{articleId}")
    suspend fun postArticleLike(
        @Body articleLikeDTO: ArticleLikeDTO
    ): Response<ArticleLikeResponse>
}