package com.example.cherry_pick_android.data.remote.service.gpt

import com.example.cherry_pick_android.data.remote.response.gpt.NewGptResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface NewGptService {
    @POST("/api/chat/new/{articleId}")
    suspend fun getNewGpt(
        @Path("articleId") articleId: Int
    ): Response<NewGptResponse>
}