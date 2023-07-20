package com.example.cherry_pick_android.util

// 플랫폼 매니저 (NAVER or KAKAO)
object PlatformManager {
    private var platform: String = ""

    fun setPlatform(platform: String){
        this.platform = platform
    }

    fun getPlatform(): String{
        return platform
    }
}