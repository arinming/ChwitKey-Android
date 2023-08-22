package com.chwitkey.cherry_pick_android.data.remote.service.user

import com.chwitkey.cherry_pick_android.data.remote.response.user.DeleteImageResponse
import retrofit2.Response
import retrofit2.http.DELETE

interface UserDeleteImageService {
    @DELETE("/api/members/deleteImage")
    suspend fun deleteImage(): Response<DeleteImageResponse>
}