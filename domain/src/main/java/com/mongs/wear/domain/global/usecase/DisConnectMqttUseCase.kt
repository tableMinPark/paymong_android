package com.mongs.wear.domain.global.usecase

import com.mongs.wear.core.exception.data.DisSubMqttException
import com.mongs.wear.core.exception.data.DisconnectMqttException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.DisConnectMqttUseCaseException
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import com.mongs.wear.domain.global.client.MqttClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DisConnectMqttUseCase @Inject constructor(
    private val mqttClient: MqttClient,
) : BaseNoParamUseCase<Unit>() {

    /**
     * 브로커 연결 해제 UseCase
     * @throws DisSubMqttException
     * @throws DisconnectMqttException
     */
    override suspend fun execute() {
        return withContext(Dispatchers.IO) {
            mqttClient.disSubManager()
            mqttClient.disSubPlayer()
            mqttClient.disSubBattleMatch()
            mqttClient.disSubSearchMatch()
            mqttClient.disConnect()
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is DisSubMqttException -> throw DisConnectMqttUseCaseException()

            is DisconnectMqttException -> throw DisConnectMqttUseCaseException()

            else -> throw DisConnectMqttUseCaseException()
        }
    }
}