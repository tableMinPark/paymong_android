package com.mongs.wear.domain.management.usecase

import com.mongs.wear.core.exception.data.DisSubMqttException
import com.mongs.wear.core.exception.data.GraduateMongException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.core.exception.usecase.GraduateMongUseCaseException
import com.mongs.wear.domain.management.repository.ManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GraduateMongUseCase @Inject constructor(
    private val mqttClient: MqttClient,
    private val managementRepository: ManagementRepository,
) : BaseParamUseCase<GraduateMongUseCase.Param, Unit>() {

    /**
     * 몽 졸업 UseCase
     * @throws DisSubMqttException
     * @throws GraduateMongException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            managementRepository.graduateMong(mongId = param.mongId)
            mqttClient.disSubManager()
        }
    }

    data class Param(
        val mongId: Long,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is DisSubMqttException -> throw GraduateMongUseCaseException()

            is GraduateMongException -> throw GraduateMongUseCaseException()

            else -> throw GraduateMongUseCaseException()
        }
    }
}