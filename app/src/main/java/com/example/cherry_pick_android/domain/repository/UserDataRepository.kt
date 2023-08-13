package com.example.cherry_pick_android.domain.repository

import androidx.lifecycle.LiveData
import com.example.cherry_pick_android.domain.model.UserData

interface UserDataRepository {
    suspend fun getUserData(): UserData
    suspend fun setUserData(key: String, value: String)

    fun observeUserData(): LiveData<UserData>
}