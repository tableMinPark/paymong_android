package com.mongs.wear.domain.auth.usecase

import com.mongs.wear.core.exception.data.LogoutException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.LogoutUseCaseException
import com.mongs.wear.domain.auth.repository.AuthRepository
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val mqttClient: MqttClient,
    private val authRepository: AuthRepository,
) : BaseNoParamUseCase<Unit>() {

    /**
     * 로그 아웃 UseCase
     * @throws LogoutException
     */
    override suspend fun execute() {

        withContext(Dispatchers.IO) {
            // 로그 아웃 처리
            authRepository.logout()

            mqttClient.disSubManager()
            mqttClient.disSubPlayer()
            mqttClient.disSubBattleMatch()
            mqttClient.disSubSearchMatch()
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is LogoutException -> throw LogoutUseCaseException()

            else -> throw LogoutUseCaseException()
        }
    }
}