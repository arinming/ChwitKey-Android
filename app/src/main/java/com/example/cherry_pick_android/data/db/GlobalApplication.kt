package com.example.cherry_pick_android.data.db

import android.app.Application

class GlobalApplication: Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        super.onCreate()

        prefs = PreferenceUtil(applicationContext)
    }
}