package com.example.cherry_pick_android.presentation.util

import android.app.Application
import android.util.Log
import com.example.cherry_pick_android.BuildConfig
import com.example.cherry_pick_android.data.remote.service.user.UserInfoService
import com.example.cherry_pick_android.domain.repository.UserDataRepository
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class ApplicationClass: Application() {
    @Inject
    lateinit var userDataRepository: UserDataRepository
    @Inject
    lateinit var userInfoService: UserInfoService

    companion object{
        var authToken: String = ""
        const val TAG = "ApplicationContext"
    }
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY) // 초기화

        CoroutineScope(Dispatchers.IO).launch {
            authToken = userDataRepository.getUserData().token
            Log.d(TAG, "토큰:$authToken")
        }

        userDataRepository.getTokenLiveData().observeForever{
            Log.d(TAG, "옵저버 감지!!: $it")
            authToken = it
        }
    }

}