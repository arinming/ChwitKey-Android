package com.example.cherry_pick_android.presentation.di

import com.example.cherry_pick_android.data.remote.repository.KeywordRepositoryImpl
import com.example.cherry_pick_android.data.remote.request.KeywordDAO
import com.example.cherry_pick_android.domain.repository.KeywordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Repository 모듈
@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideKeywordRepository(keywordDAO: KeywordDAO): KeywordRepository{
        return KeywordRepositoryImpl(keywordDAO)
    }
}