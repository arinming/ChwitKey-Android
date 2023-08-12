package com.example.cherry_pick_android.data.module.datastore

import android.content.Context
import com.example.cherry_pick_android.data.remote.repository.UserIdRepositoryImpl
import com.example.cherry_pick_android.domain.repository.UserIdRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserIdDataStore(
        @ApplicationContext context: Context
    ): UserIdRepository{
        return UserIdRepositoryImpl(context)
    }
}