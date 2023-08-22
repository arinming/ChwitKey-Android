package com.chwitkey.cherry_pick_android.data.remote.service.login

import com.chwitkey.cherry_pick_android.data.remote.request.login.SignInRequest
import com.chwitkey.cherry_pick_android.data.remote.response.login.SignInResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignInService {
    @POST("/api/members/signIn")
    suspend fun signInInform(
        @Body signInRequest: SignInRequest
    ): Response<SignInResponse>
}