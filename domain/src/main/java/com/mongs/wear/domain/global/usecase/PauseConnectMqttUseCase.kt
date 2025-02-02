package com.mongs.wear.domain.global.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.domain.global.exception.PauseConnectMqttException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PauseConnectMqttUseCase @Inject constructor(
    private val mqttClient: MqttClient,
) : BaseNoParamUseCase<Unit>() {

    override suspend fun execute() {

        return withContext(Dispatchers.IO) {
            mqttClient.disSubManager()
            mqttClient.disSubPlayer()
            mqttClient.disSubBattleMatch()
            mqttClient.disSubSearchMatch()
            mqttClient.pauseConnect()
        }
    }

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw PauseConnectMqttException()
        }
    }
}