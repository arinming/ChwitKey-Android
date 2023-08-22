package com.chwitkey.cherry_pick_android.presentation.di

import android.app.Application
import androidx.room.Room
import com.chwitkey.cherry_pick_android.data.db.KeywordDAO
import com.chwitkey.cherry_pick_android.data.db.KeywordDB
import com.chwitkey.cherry_pick_android.data.db.RecordDAO
import com.chwitkey.cherry_pick_android.data.db.RecordDB
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
    fun provideRecordDatabase(application: Application): RecordDB {
        return Room.databaseBuilder(
            application,
            RecordDB::class.java,
            "record_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideKeywordDAO(keywordDB: KeywordDB): KeywordDAO {
        return keywordDB.keywordDAO()
    }

    @Singleton
    @Provides
    fun provideRecordDAO(recordDB: RecordDB): RecordDAO {
        return recordDB.recordDAO()
    }
}