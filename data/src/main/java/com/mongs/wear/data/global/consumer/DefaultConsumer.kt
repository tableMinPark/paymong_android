package com.mongs.wear.data.global.consumer

import android.util.Log
import com.google.gson.Gson
import com.mongs.wear.core.dto.response.ResponseDto
import com.mongs.wear.data.activity.consumer.BattleConsumer
import com.mongs.wear.data.device.datastore.DeviceDataStore
import com.mongs.wear.data.global.client.MqttClientImpl
import com.mongs.wear.data.manager.consumer.ManagementConsumer
import com.mongs.wear.data.user.consumer.PlayerConsumer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultConsumer @Inject constructor(
    private val managementConsumer: ManagementConsumer,
    private val playerConsumer: PlayerConsumer,
    private val battleConsumer: BattleConsumer,
    private val deviceDataStore: DeviceDataStore,
    private val gson: Gson,
) : MqttCallback {

    companion object {
        private const val TAG = "DefaultConsumer"

        private const val ACTIVITY_BATTLE_PREFIX = "ACTIVITY-BATTLE-"
        private const val USER_PLAYER_PREFIX = "USER-PLAYER-"
        private const val MANAGER_MANAGEMENT_PREFIX = "MANAGER-MANAGEMENT-"
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        message?.let {
            topic?.let {
                try {
                    val responseDto = gson.fromJson(message.toString(), ResponseDto::class.java)

                    Log.i(TAG, "[Consume] ${responseDto.message} : ${responseDto.result}")

                    responseDto.code?.let { code ->
                        val resultJson = gson.toJson(responseDto.result)

                        if (code.startsWith(MANAGER_MANAGEMENT_PREFIX)) {
                            managementConsumer.messageArrived(
                                code = code,
                                resultJson = resultJson
                            )
                        } else if (responseDto.code?.startsWith(USER_PLAYER_PREFIX) == true) {
                            playerConsumer.messageArrived(
                                code = code,
                                resultJson = resultJson
                            )
                        } else if (responseDto.code?.startsWith(ACTIVITY_BATTLE_PREFIX) == true) {
                            battleConsumer.messageArrived(
                                code = code,
                                resultJson = resultJson
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "[Consume] mqtt message 파싱 실패")
                }
            }
        }
    }

    override fun connectionLost(cause: Throwable?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (MqttClientImpl.mqttState.broker != MqttClientImpl.MqttState.MqttStateCode.PAUSE_DISCONNECT) {
                MqttClientImpl.mqttState.broker = MqttClientImpl.MqttState.MqttStateCode.DISCONNECT
                deviceDataStore.setNetwork(network = false)
            }
        }
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {}
}