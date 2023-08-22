package com.chwitkey.cherry_pick_android.data.remote.service.user

import com.chwitkey.cherry_pick_android.data.remote.request.user.InitUserSaveRequest
import com.chwitkey.cherry_pick_android.data.remote.response.user.InitUserSaveResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface InitUserSaveService {
    @POST("/api/members/signUp")
    suspend fun putInitUserData(
        @Body initUserSaveRequest: InitUserSaveRequest
    ): Response<InitUserSaveResponse>
}