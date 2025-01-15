package com.mongs.wear.data.device.module

import com.mongs.wear.data.device.api.DeviceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeviceApiModule {

    @Provides
    @Singleton
    fun provideCollectionApi(retrofit: Retrofit): DeviceApi = retrofit.create(DeviceApi::class.java)
}