package com.example.cherry_pick_android.data.remote.service.user

import com.example.cherry_pick_android.data.remote.response.user.ApiResponse
import retrofit2.Response
import retrofit2.http.DELETE

interface DeleteUserService {
    @DELETE("/api/members/deleteMember")
    suspend fun deleteUser(): Response<ApiResponse>
}