package com.chwitkey.cherry_pick_android.data.remote.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chwitkey.cherry_pick_android.domain.model.UserData
import com.chwitkey.cherry_pick_android.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException
import java.lang.IllegalArgumentException
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val context: Context
): UserDataRepository {

    // Data stroe 생성
    private val Context.dataStore by preferencesDataStore(name = "user_data")
    // 각종 LiveData 선언
    private val _tokenLiveData = MutableLiveData<String>()
    private val _userId = MutableLiveData<String>()
    private val _platForm = MutableLiveData<String>()
    private val _name = MutableLiveData<String>()
    private val _birthday = MutableLiveData<String>()

    companion object{
        const val TAG = "UserIdRepositoryImpl"
        private val USER_KEY = stringPreferencesKey("userId")
        private val PLATFORM_KEY = stringPreferencesKey("platform")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val GENDER_KEY = stringPreferencesKey("gender")
        private val BIRTHDAY_KEY = stringPreferencesKey("birthday")
        private val ISINIT_KEY = stringPreferencesKey("isInit")
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
                "userId" -> {
                    _userId.postValue(value)
                    USER_KEY
                }
                "platform" -> {
                    _platForm.postValue(value)
                    PLATFORM_KEY
                }
                "token" -> {
                    _tokenLiveData.postValue(value)
                    TOKEN_KEY
                }
                "name" -> {
                    _name.postValue(value)
                    NAME_KEY
                }
                "gender" -> GENDER_KEY
                "birthday" -> {
                    _birthday.postValue(value)
                    BIRTHDAY_KEY
                }
                "isInit" -> ISINIT_KEY
                else -> throw IllegalArgumentException("Unknown key: $key")
            }
            Log.d(TAG, "Key:${key} Value:${value}")
            preferences[preferencesKey] = value
        }
    }

    override fun getTokenLiveData(): LiveData<String> {
        return _tokenLiveData
    }

    override fun getUserIdLiveData(): LiveData<String> {
        return _userId
    }

    override fun getPlatFormLiveData(): LiveData<String> {
        return _platForm
    }

    override fun getNameLiveData(): LiveData<String> {
        return _name
    }

    override fun getBirthLiveData(): LiveData<String> {
        return _birthday
    }


    // 유저 정보 매핑
    private fun mapperToUserData(preferences: Preferences): UserData {
        val userId = preferences[USER_KEY] ?: ""
        val platform = preferences[PLATFORM_KEY] ?: ""
        val token = preferences[TOKEN_KEY] ?: ""
        val name = preferences[NAME_KEY] ?: ""
        val gender = preferences[GENDER_KEY] ?: ""
        val birthday = preferences[BIRTHDAY_KEY] ?: ""
        val isInit = preferences[ISINIT_KEY] ?: ""

        return UserData(userId, platform, token, name, gender, birthday, isInit)
    }

}
