package com.example.cherry_pick_android.data.remote.service.login

import com.example.cherry_pick_android.data.remote.response.login.UserInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserInfoService {
    @GET("/api/members/info/{user_id}")
    suspend fun getUserInfo(
        @Path("user_id") user_id: String
    ): Response<UserInfoResponse>
}