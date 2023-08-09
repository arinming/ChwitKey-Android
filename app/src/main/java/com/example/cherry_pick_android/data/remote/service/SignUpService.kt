package com.example.cherry_pick_android.data.remote.service

import com.example.cherry_pick_android.data.remote.request.SignUpRequest
import com.example.cherry_pick_android.data.remote.response.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpService {
    @POST("/signup")
    suspend fun getSignUp(
        @Body data: SignUpRequest
    ): Call<SignUpResponse>
}