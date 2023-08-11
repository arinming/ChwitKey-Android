package com.example.cherry_pick_android.presentation.di

import com.example.cherry_pick_android.data.remote.repository.KeywordRepositoryImpl
import com.example.cherry_pick_android.data.db.KeywordDAO
import com.example.cherry_pick_android.data.db.RecordDAO
import com.example.cherry_pick_android.data.remote.repository.RecordRepositoryImpl
import com.example.cherry_pick_android.domain.repository.KeywordRepository
import com.example.cherry_pick_android.domain.repository.RecordRepository
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

    @Singleton
    @Provides
    fun provideRecordRepository(recordDAO: RecordDAO): RecordRepository {
        return RecordRepositoryImpl(recordDAO)
    }
}