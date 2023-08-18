package com.example.cherry_pick_android.data.module.api

import android.util.Log
import com.example.cherry_pick_android.domain.repository.UserDataRepository
import com.example.cherry_pick_android.presentation.util.ApplicationClass
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import javax.inject.Inject

class AppInterceptor: Interceptor {
    companion object{
        const val TAG = "AppInterceptor"
    }
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val authToken = ApplicationClass.authToken
        Log.d(TAG, authToken)
        val request = chain.request().newBuilder()
            .addHeader("Authorization", authToken)
            .build()
        return chain.proceed(request)
    }
}