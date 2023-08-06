package com.example.cherry_pick_android.data.api

import com.example.cherry_pick_android.data.data.SignUp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/signup")
    suspend fun getSignUp(
        @Body data: SignUp
    ): Call<SignUp>
}