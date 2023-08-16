package com.example.cherry_pick_android.data.module.api


import com.example.cherry_pick_android.data.remote.repository.ArticleRepository
import com.example.cherry_pick_android.data.remote.service.ArticleSearchService
import com.example.cherry_pick_android.data.remote.service.login.SignInService
import com.example.cherry_pick_android.data.remote.service.user.DeleteUserService
import com.example.cherry_pick_android.data.remote.service.user.InitUserSaveService
import com.example.cherry_pick_android.data.remote.service.user.UserInfoService
import com.example.cherry_pick_android.data.remote.service.user.UserKeywordService
import com.example.cherry_pick_android.data.remote.service.user.UserNameUpdateService
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
    private val BASE_URL = "https://umcserver.shop"

    @Singleton
    @Provides
    fun getInterceptor(): AppInterceptor{
        return AppInterceptor()
    }
    @Singleton
    @Provides
    fun getOkHttpClient(interceptor: AppInterceptor): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
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
            .client(getOkHttpClient(getInterceptor()))
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideSignInService(retrofit: Retrofit): SignInService {
        return retrofit.create(SignInService::class.java)
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

    @Provides
    @Singleton
    fun provideUserInfo(retrofit: Retrofit): UserInfoService {
        return retrofit.create(UserInfoService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserKeyword(retrofit: Retrofit): UserKeywordService{
        return retrofit.create(UserKeywordService::class.java)
    }

    @Provides
    @Singleton
    fun provideInitUserSave(retrofit: Retrofit): InitUserSaveService{
        return retrofit.create(InitUserSaveService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserDelete(retrofit: Retrofit): DeleteUserService{
        return retrofit.create(DeleteUserService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserNameUpdate(retrofit: Retrofit): UserNameUpdateService{
        return retrofit.create(UserNameUpdateService::class.java)
    }
}