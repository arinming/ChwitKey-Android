package com.example.cherry_pick_android.data.remote.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.cherry_pick_android.domain.model.UserId
import com.example.cherry_pick_android.domain.repository.UserIdRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException
import java.lang.IllegalArgumentException
import javax.inject.Inject

class UserIdRepositoryImpl @Inject constructor(
    private val context: Context
): UserIdRepository {

    private val Context.dataStore by preferencesDataStore(name = "user_id")

    companion object{
        const val TAG = "UserIdRepositoryImpl"
        private val KAKAO_KEY = stringPreferencesKey("kakao")
        private val NAVER_KEY = stringPreferencesKey("naver")
    }

    override suspend fun getUserId(): UserId {
        val userId = context.dataStore.data
            .catch {exception ->
                if(exception is IOException){
                    Log.d(TAG, exception.toString())
                    emit(emptyPreferences())
                }else{
                    throw exception
                }
            }.map { preferences ->
                mapperToUserId(preferences)
            }.first()
        return userId
    }

    override suspend fun setUserId(key: String, value: String) {
        context.dataStore.edit { preferences ->
            val preferencesKey = when(key){
                "kakao" -> KAKAO_KEY
                "naver" -> NAVER_KEY
                else -> throw IllegalArgumentException("Unknown key: $key")
            }
            preferences[preferencesKey] = value
        }
    }

    private fun mapperToUserId(preferences: Preferences): UserId {
        val kakaoUserId = preferences[KAKAO_KEY] ?: ""
        val naverUserId = preferences[NAVER_KEY] ?: ""

        return UserId(kakaoUserId, naverUserId)
    }

}