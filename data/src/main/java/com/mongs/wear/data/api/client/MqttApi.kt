package com.mongs.wear.data.api.client

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MqttApi @Inject constructor(
    private val mqttAndroidClient: MqttAndroidClient
) {
    suspend fun connect(messageCallback: MqttCallback) {
        withContext(Dispatchers.IO) {
            val options = MqttConnectOptions().apply {
//                this.userName = username
//                this.password = password?.toCharArray()
            }

            try {
                mqttAndroidClient.setCallback(messageCallback)
                mqttAndroidClient.connect(options).await()
                Log.i("MqttApi", "connect.")
            } catch (e: MqttException) {
                Log.e("MqttApi", "connect fail.")
            }
        }
    }

    suspend fun disConnect() {
        withContext(Dispatchers.IO) {
            if (mqttAndroidClient.isConnected) {
                mqttAndroidClient.disconnect().await()
                Log.i("MqttApi", "disConnect.")
            } else {
                Log.e("MqttApi", "disConnect fail.")
            }
        }
    }

    suspend fun subscribe(topic: String) {
        withContext(Dispatchers.IO) {
            if (mqttAndroidClient.isConnected) {
                mqttAndroidClient.subscribe(topic, 2).await()
                Log.i("MqttApi", "[$topic] subscribe.")
            } else {
                Log.e("MqttApi", "[$topic] subscribe fail.")
            }
        }
    }

    suspend fun disSubscribe(topic: String) {
        withContext(Dispatchers.IO) {
            if (mqttAndroidClient.isConnected) {
                mqttAndroidClient.unsubscribe(topic).await()
                Log.i("MqttApi", "[$topic] unSubscribe.")
            } else {
                Log.e("MqttApi", "[$topic] unSubscribe fail.")
            }
        }
    }

    suspend fun produce(topic: String, payload: String) {
        withContext(Dispatchers.IO) {
            if (mqttAndroidClient.isConnected) {
                mqttAndroidClient.publish(topic = topic, payload = payload.toByteArray(), qos = 2, retained = true).await()
                Log.i("MqttApi", "[$topic] produce.")
            } else {
                Log.e("MqttApi", "[$topic] produce fail.")
            }
        }
    }

    private suspend fun IMqttToken.await() = suspendCancellableCoroutine { cont ->
        actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                cont.resume(Unit)
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                cont.resumeWithException(exception ?: Exception("Unknown error"))
            }
        }
    }
}