package com.mongs.wear.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mongs.wear.activity.MainActivity
import com.mongs.wear.activity.MainApplication
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.module.NotificationModule
import com.mongs.wear.presentation.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class NotificationService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "NotificationService"
    }

    @Inject lateinit var notificationManager: NotificationManager

    @Inject lateinit var deviceRepository: DeviceRepository

    override fun onNewToken(token: String) {}

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        CoroutineScope(Dispatchers.IO).launch {
            if (remoteMessage.data.isNotEmpty() && deviceRepository.getNotification()) {

                val isAppForeground = (applicationContext as MainApplication).isAppForeground
                val isAppForegroundMessage = remoteMessage.data["isAppForegroundMessage"].toBoolean()

                val messageShow = isAppForeground == isAppForegroundMessage

                if (messageShow) {
                    sendNotification(
                        title = remoteMessage.data["title"].toString(),
                        body = remoteMessage.data["body"].toString(),
                    )
                }
            }
        }
    }

    private fun sendNotification(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, NotificationModule.CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_not_open)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0, notificationBuilder.build())
    }
}