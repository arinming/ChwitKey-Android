package com.chwitkey.cherry_pick_android.data.module.api


import android.util.Log
import com.chwitkey.cherry_pick_android.data.remote.service.article.ArticleDetailService
import com.chwitkey.cherry_pick_android.data.remote.service.article.ArticleLikeService
import com.chwitkey.cherry_pick_android.data.remote.service.article.ArticleScrapService
import com.chwitkey.cherry_pick_android.data.remote.service.article.ArticleSearchCommendService
import com.chwitkey.cherry_pick_android.data.remote.service.article.ArticleSearchIndustryService
import com.chwitkey.cherry_pick_android.data.remote.service.article.ArticleUnlikeService
import com.chwitkey.cherry_pick_android.data.remote.service.article.ArticleSearchKeywordService
import com.chwitkey.cherry_pick_android.data.remote.service.gpt.GptQnaService
import com.chwitkey.cherry_pick_android.data.remote.service.gpt.GptSelectService
import com.chwitkey.cherry_pick_android.data.remote.service.gpt.NewGptService
import com.chwitkey.cherry_pick_android.data.remote.service.login.SignInService
import com.chwitkey.cherry_pick_android.data.remote.service.user.DeleteUserService
import com.chwitkey.cherry_pick_android.data.remote.service.user.InitUserSaveService
import com.chwitkey.cherry_pick_android.data.remote.service.user.UpdateIndustryService
import com.chwitkey.cherry_pick_android.data.remote.service.user.UpLoadImageService
import com.chwitkey.cherry_pick_android.data.remote.service.user.UserDeleteImageService
import com.chwitkey.cherry_pick_android.data.remote.service.user.UserInfoService
import com.chwitkey.cherry_pick_android.data.remote.service.user.UserKeywordService
import com.chwitkey.cherry_pick_android.data.remote.service.user.UserNameUpdateService
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
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    private val BASE_URL = "https://umcserver.shop"
    companion object{
        const val TAG = "ApiModule"
    }

    @Singleton
    @Provides
    fun getInterceptor(): AppInterceptor{
        Log.d(TAG, "getInterceptor")
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
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
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
    fun provideArticleSearchService(retrofit: Retrofit): ArticleSearchCommendService {
        return retrofit.create(ArticleSearchCommendService::class.java)
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

    @Provides
    @Singleton
    fun provideArticleDetailService(retrofit: Retrofit): ArticleDetailService {
        return retrofit.create(ArticleDetailService::class.java)
    }

    @Provides
    @Singleton
    fun provideUpdateIndustry(retrofit: Retrofit): UpdateIndustryService {
        return retrofit.create(UpdateIndustryService::class.java)
    }

    @Provides
    @Singleton
    fun provideScrapList(retrofit: Retrofit): ArticleScrapService{
        return retrofit.create(ArticleScrapService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserImageUpload(retrofit: Retrofit): UpLoadImageService{
        return retrofit.create(UpLoadImageService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewGpt(retrofit: Retrofit): NewGptService{
        return retrofit.create(NewGptService::class.java)
    }

    @Provides
    @Singleton
    fun provideSelectGpt(retrofit: Retrofit): GptSelectService{
        return retrofit.create(GptSelectService::class.java)
    }

    @Provides
    @Singleton
    fun provideGptQna(retrofit: Retrofit): GptQnaService{
        return retrofit.create(GptQnaService::class.java)
    }

    @Provides
    @Singleton
    fun provideLikeArticle(retrofit: Retrofit): ArticleLikeService{
        return retrofit.create(ArticleLikeService::class.java)
    }

    @Provides
    @Singleton
    fun provideUnLikeArticle(retrofit: Retrofit): ArticleUnlikeService{
        return retrofit.create(ArticleUnlikeService::class.java)
    }

    @Provides
    @Singleton
    fun provideKeywordArticle(retrofit: Retrofit): ArticleSearchKeywordService {
        return retrofit.create(ArticleSearchKeywordService::class.java)
    }

    @Provides
    @Singleton
    fun provideIndustryArticle(retrofit: Retrofit): ArticleSearchIndustryService {
        return retrofit.create(ArticleSearchIndustryService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserDeleteImage(retrofit: Retrofit): UserDeleteImageService {
        return retrofit.create(UserDeleteImageService::class.java)
    }

}