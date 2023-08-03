package com.example.cherry_pick_android.presentation.di

import android.app.Application
import androidx.room.Room
import com.example.cherry_pick_android.data.db.KeywordDAO
import com.example.cherry_pick_android.data.db.KeywordDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// DB 관련 의존성 모듈
@InstallIn(SingletonComponent::class)
@Module
class LocalDataModule {
    @Singleton
    @Provides
    fun provideKeywordDatabase(application: Application): KeywordDB {
        return Room.databaseBuilder(
            application,
            KeywordDB::class.java,
            "keyword_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideKeywordDAO(keywordDB: KeywordDB): KeywordDAO {
        return keywordDB.keywordDAO()
    }
}