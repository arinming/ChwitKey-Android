package com.example.cherry_pick_android.data.remote.service.gpt

import com.example.cherry_pick_android.data.remote.request.gpt.GptQnaRequset
import com.example.cherry_pick_android.data.remote.response.gpt.GptQnaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GptQnaService {
    @POST("/api/chat/qna")
    suspend fun getAnswer(
        @Body gptQnaRequset: GptQnaRequset
    ): Response<GptQnaResponse>
}