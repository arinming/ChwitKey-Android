package com.chwitkey.cherry_pick_android.data.remote.service.user

import com.chwitkey.cherry_pick_android.data.remote.response.user.UserKeywordResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserKeywordService {
    @GET("/api/members/keyword")
    suspend fun getUserKeyword(): Response<UserKeywordResponse>
}