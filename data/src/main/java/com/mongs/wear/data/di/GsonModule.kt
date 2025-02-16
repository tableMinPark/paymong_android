package com.mongs.wear.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mongs.wear.data.utils.GsonDateFormatAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.LocalDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GsonModule {
    @Provides
    @Singleton
    fun provideGson() : Gson {
        return GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, GsonDateFormatAdapter())
            .create()
    }
}