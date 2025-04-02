package com.sonchan.photoretouching.di

import android.app.Activity
import com.sonchan.photoretouching.data.repository.main.GalleryRepositoryImpl
import com.sonchan.photoretouching.domain.repository.main.GalleryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    fun provideGalleryRepository(activity: Activity): GalleryRepository {
        return GalleryRepositoryImpl(activity)
    }
}