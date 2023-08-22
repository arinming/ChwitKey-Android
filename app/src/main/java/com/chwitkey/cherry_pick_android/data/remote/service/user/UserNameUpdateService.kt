package com.chwitkey.cherry_pick_android.data.remote.service.user

import com.chwitkey.cherry_pick_android.data.remote.request.user.updateNameRequest
import com.chwitkey.cherry_pick_android.data.remote.response.user.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserNameUpdateService {
    @POST("/api/members/updateName")
    suspend fun putUserName(
        @Body updateName: updateNameRequest
    ): Response<ApiResponse>
}