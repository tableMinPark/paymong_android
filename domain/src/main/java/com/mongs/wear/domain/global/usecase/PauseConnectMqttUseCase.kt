package com.mongs.wear.domain.global.usecase

import com.mongs.wear.core.exception.data.DisSubMqttException
import com.mongs.wear.core.exception.data.PauseMqttException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.core.exception.usecase.PauseConnectMqttUseCaseException
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PauseConnectMqttUseCase @Inject constructor(
    private val mqttClient: MqttClient,
) : BaseNoParamUseCase<Unit>() {

    /**
     * 브로커 일시 중지 UseCase
     * @throws DisSubMqttException
     * @throws PauseConnectMqttUseCase
     */
    override suspend fun execute() {
        return withContext(Dispatchers.IO) {
            mqttClient.disSubManager()
            mqttClient.disSubPlayer()
            mqttClient.disSubBattleMatch()
            mqttClient.disSubSearchMatch()
            mqttClient.pauseConnect()
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is DisSubMqttException -> throw PauseConnectMqttUseCaseException()

            is PauseMqttException -> throw PauseConnectMqttUseCaseException()

            else -> throw PauseConnectMqttUseCaseException()
        }
    }
}