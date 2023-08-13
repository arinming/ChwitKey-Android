package com.example.cherry_pick_android.data.module.api


import com.example.cherry_pick_android.data.remote.repository.ArticleRepository
import com.example.cherry_pick_android.data.remote.service.ArticleSearchService
import com.example.cherry_pick_android.data.remote.service.SignUpService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton
import com.squareup.moshi.Moshi

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    private val BASE_URL = "http://3.37.104.185"

    @Singleton
    @Provides
    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .build()
    }

    @Singleton
    @Provides
    fun getInstance(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun getSignUpService(retrofit: Retrofit): SignUpService {
        return retrofit.create(SignUpService::class.java)
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
