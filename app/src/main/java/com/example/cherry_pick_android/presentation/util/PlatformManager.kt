package com.example.cherry_pick_android.presentation.util

// 플랫폼 매니저 (NAVER or KAKAO)
object PlatformManager {
    private var platform: String = ""

    fun setPlatform(platform: String){
        PlatformManager.platform = platform
    }

    fun getPlatform(): String{
        return platform
    }
}