package com.example.cherry_pick_android.presentation.util

import android.app.Application
import android.util.Log
import com.example.cherry_pick_android.BuildConfig
import com.example.cherry_pick_android.domain.repository.UserDataRepository
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class ApplicationClass: Application() {
    @Inject
    lateinit var userDataRepository: UserDataRepository
    companion object{
        var authToken: String = ""
    }
    override fun onCreate() {
        super.onCreate()

        // 키 해서 얻기
        val keyHash = Utility.getKeyHash(this)
        Log.d("HASHKEY", keyHash)

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY) // 초기화

        userDataRepository.getTokenLiveData().observeForever{
            Log.d("ApplicationContext", "옵저버 감지!!: $it")
            authToken = it
        }
    }

}