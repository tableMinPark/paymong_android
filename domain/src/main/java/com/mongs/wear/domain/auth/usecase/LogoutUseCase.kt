package com.mongs.wear.domain.auth.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.auth.exception.JoinException
import com.mongs.wear.domain.auth.exception.LogoutException
import com.mongs.wear.domain.auth.repository.AuthRepository
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.domain.global.usecase.BaseNoParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val mqttClient: MqttClient,
    private val authRepository: AuthRepository,
) : BaseNoParamUseCase<Unit>() {

    override suspend fun execute() {

        withContext(Dispatchers.IO) {

            authRepository.logout()

            mqttClient.disSubManager()
            mqttClient.disSubPlayer()
            mqttClient.disSubBattleMatch()
            mqttClient.disSubSearchMatch()
        }
    }

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw LogoutException()
        }
    }
}