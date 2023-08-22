package com.chwitkey.cherry_pick_android.data.remote.service.gpt

import com.chwitkey.cherry_pick_android.data.remote.response.gpt.GptSelectResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GptSelectService {
    @GET("/api/chat/select/{articleId}")
    suspend fun getSelectMessage(
        @Path("articleId") articleId: Int,
        @Query("type") type: String
    ):Response<GptSelectResponse>
}