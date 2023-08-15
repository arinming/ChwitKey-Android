package com.example.cherry_pick_android.data.remote.service.user

import com.example.cherry_pick_android.data.remote.response.user.UserInfoResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserInfoService {
    @GET("/api/members/info")
    suspend fun getUserInfo(): Response<UserInfoResponse>
}