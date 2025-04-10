package com.sonchan.photoretouching.di

import android.content.Context
import com.sonchan.photoretouching.data.datastore.preference.ThemePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Provides
    @Singleton
    fun provideThemePreference(@ApplicationContext context: Context): ThemePreference{
        return ThemePreference(context)
    }
}