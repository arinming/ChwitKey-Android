package com.example.cherry_pick_android.data.remote.service.article

import com.example.cherry_pick_android.data.remote.request.ArticleNewDTO
import com.example.cherry_pick_android.data.remote.response.ArticleNewResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// 기사 추가 요청
interface ArticleNewService {
    @POST("/api/articles/new")
    suspend fun postArticleNew(
        @Body articleNewDTO: ArticleNewDTO
    ): Response<ArticleNewResponse>
}