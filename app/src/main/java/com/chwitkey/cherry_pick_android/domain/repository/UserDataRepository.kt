package com.chwitkey.cherry_pick_android.domain.repository

import androidx.lifecycle.LiveData
import com.chwitkey.cherry_pick_android.domain.model.UserData

interface UserDataRepository {
    suspend fun getUserData(): UserData
    suspend fun setUserData(key: String, value: String)

    fun getTokenLiveData(): LiveData<String>

    fun getUserIdLiveData(): LiveData<String>
    fun getPlatFormLiveData(): LiveData<String>

    fun getNameLiveData(): LiveData<String>

    fun getBirthLiveData(): LiveData<String>
}