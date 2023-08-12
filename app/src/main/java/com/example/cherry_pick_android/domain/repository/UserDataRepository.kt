package com.example.cherry_pick_android.domain.repository

import com.example.cherry_pick_android.domain.model.UserData

interface UserDataRepository {
    suspend fun getUserData(): UserData
    suspend fun setUserData(key: String, value: String)
}