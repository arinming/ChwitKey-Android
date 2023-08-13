package com.example.cherry_pick_android.domain.repository

import com.example.cherry_pick_android.domain.model.UserId

interface UserIdRepository {
    suspend fun getUserId(): UserId
    suspend fun setUserId(key: String, value: String)
}