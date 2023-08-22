package com.chwitkey.cherry_pick_android.data.module.datastore

import android.content.Context
import com.chwitkey.cherry_pick_android.data.remote.repository.UserDataRepositoryImpl
import com.chwitkey.cherry_pick_android.domain.repository.UserDataRepository
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
    ): UserDataRepository{
        return UserDataRepositoryImpl(context)
    }
}