package com.example.cherry_pick_android.data.remote.service

import com.example.cherry_pick_android.data.remote.request.ArticleLikeDTO
import com.example.cherry_pick_android.data.remote.response.ArticleLikeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// 관심 기사 요청
interface ArticleLikeService {
    @POST("/api/articles/like/{articleId}")
    suspend fun postArticleLike(
        @Body articleLikeDTO: ArticleLikeDTO
    ): Call<ArticleLikeResponse>
}