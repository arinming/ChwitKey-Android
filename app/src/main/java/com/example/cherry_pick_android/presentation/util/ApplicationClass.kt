package com.example.cherry_pick_android.presentation.util

import android.app.Application
import android.util.Log
import com.example.cherry_pick_android.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass: Application() {
    override fun onCreate() {
        super.onCreate()

        // 키 해서 얻기
        val keyHash = Utility.getKeyHash(this)
        Log.d("HASHKEY", keyHash)

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY) // 초기화
    }

}