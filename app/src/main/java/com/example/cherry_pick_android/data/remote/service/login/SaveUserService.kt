package com.example.cherry_pick_android.data.remote.service.login

import com.example.cherry_pick_android.data.remote.request.login.SaveUserRequest
import com.example.cherry_pick_android.data.remote.response.login.SaveUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SaveUserService {
    @POST("/api/members/save")
    suspend fun saveUserInform(
        @Body saveUserRequest: SaveUserRequest
    ): Response<SaveUserResponse>
}