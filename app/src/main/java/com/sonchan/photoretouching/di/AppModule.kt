package com.sonchan.photoretouching.di

import android.app.Activity
import com.sonchan.photoretouching.data.datasource.main.GalleryDataSource
import com.sonchan.photoretouching.data.datasource.main.GalleryDataSourceImpl
import com.sonchan.photoretouching.data.repository.main.GalleryRepositoryImpl
import com.sonchan.photoretouching.domain.repository.main.GalleryRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindGalleryDataSource(
        impl: GalleryDataSourceImpl
    ): GalleryDataSource

    @Binds
    @Singleton
    abstract fun bindGalleryRepository(
        impl: GalleryRepositoryImpl
    ): GalleryRepository
}