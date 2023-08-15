package com.example.cherry_pick_android.data.remote.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cherry_pick_android.domain.model.UserData
import com.example.cherry_pick_android.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException
import java.lang.IllegalArgumentException
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val context: Context
): UserDataRepository {

    private val Context.dataStore by preferencesDataStore(name = "user_data")
    private val _userData = MutableLiveData<UserData>()

    companion object{
        const val TAG = "UserIdRepositoryImpl"
        private val USER_KEY = stringPreferencesKey("userId")
        private val PLATFORM_KEY = stringPreferencesKey("platform")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val GENDER_KEY = stringPreferencesKey("gender")
        private val BIRTHDAY_KEY = stringPreferencesKey("birthday")
    }

    // 유저 정보 흭득
    override suspend fun getUserData(): UserData {
        val userData = context.dataStore.data
            .catch {exception ->
                if(exception is IOException){
                    Log.d(TAG, exception.toString())
                    emit(emptyPreferences())
                }else{
                    throw exception
                }
            }.map { preferences ->
                mapperToUserData(preferences)
            }.first()
        return userData
    }

    // 유저 정보 세팅
    override suspend fun setUserData(key: String, value: String) {
        context.dataStore.edit { preferences ->
            val preferencesKey = when(key){
                "userId" -> USER_KEY
                "platform" -> PLATFORM_KEY
                "token" -> TOKEN_KEY
                "name" -> NAME_KEY
                "gender" -> GENDER_KEY
                "birthday" -> BIRTHDAY_KEY
                else -> throw IllegalArgumentException("Unknown key: $key")
            }
            Log.d(TAG, "Key:${key} Value:${value}")
            preferences[preferencesKey] = value
            _userData.postValue(mapperToUserData(preferences))
        }
    }


    // 유저 정보 매핑
    private fun mapperToUserData(preferences: Preferences): UserData {
        val userId = preferences[USER_KEY] ?: ""
        val platform = preferences[PLATFORM_KEY] ?: ""
        val token = preferences[TOKEN_KEY] ?: ""
        val name = preferences[NAME_KEY] ?: ""
        val gender = preferences[GENDER_KEY] ?: ""
        val birthday = preferences[BIRTHDAY_KEY] ?: ""

        return UserData(userId, platform, token, name, gender, birthday)
    }

}