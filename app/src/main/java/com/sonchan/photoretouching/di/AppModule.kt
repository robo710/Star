package com.sonchan.photoretouching.di

import android.content.ContentResolver
import android.content.Context
import com.sonchan.photoretouching.data.datasource.main.RetouchingDataSource
import com.sonchan.photoretouching.data.datasource.main.RetouchingDataSourceImpl
import com.sonchan.photoretouching.data.repository.main.RetouchingRepositoryImpl
import com.sonchan.photoretouching.domain.repository.main.RetouchingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindRetouchingDataSource(
        impl: RetouchingDataSourceImpl
    ): RetouchingDataSource

    @Binds
    @Singleton
    abstract fun bindRetouchingRepository(
        impl: RetouchingRepositoryImpl
    ): RetouchingRepository
}