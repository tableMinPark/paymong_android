package com.mongs.wear.module

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    private const val TAG = "ServiceModule"

    const val CHANNEL_ID = "mongs_default_channel"
    private const val CHANNEL_NAME = "Mongs Default Channel"

    @Provides
    @Singleton
    fun provideFirebaseMessaging() : FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context) : NotificationManager {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT))

        return notificationManager
    }
}