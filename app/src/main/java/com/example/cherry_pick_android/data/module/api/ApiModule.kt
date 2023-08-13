package com.example.cherry_pick_android.data.module.api


import com.example.cherry_pick_android.data.remote.repository.ArticleRepository
import com.example.cherry_pick_android.data.remote.service.ArticleSearchService
import com.example.cherry_pick_android.data.remote.service.login.SaveUserService
import com.example.cherry_pick_android.data.remote.service.login.UserInfoService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    private val BASE_URL = "http://3.37.104.185"

    @Singleton
    @Provides
    fun getOkHttpClient(): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Singleton
    @Provides
    fun getInstance(okHttpClient: OkHttpClient): Retrofit{
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(getOkHttpClient())
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun saveUserService(retrofit: Retrofit): SaveUserService {
        return retrofit.create(SaveUserService::class.java)
    }

    @Singleton
    @Provides
    fun userInfoService(retrofit: Retrofit): UserInfoService{
        return retrofit.create(UserInfoService::class.java)
    }



    @Provides
    @Singleton
    fun provideArticleSearchService(retrofit: Retrofit): ArticleSearchService {
        return retrofit.create(ArticleSearchService::class.java)
    }

    @Provides
    @Singleton
    fun provideArticleRepository(articleSearchService: ArticleSearchService): ArticleRepository {
        return ArticleRepository(articleSearchService)
    }
}