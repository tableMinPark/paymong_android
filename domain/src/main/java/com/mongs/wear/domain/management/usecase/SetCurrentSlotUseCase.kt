package com.mongs.wear.domain.management.usecase

import com.mongs.wear.core.exception.data.DisSubMqttException
import com.mongs.wear.core.exception.data.SubMqttException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.SetCurrentSlotUseCaseException
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.domain.management.repository.ManagementRepository
import com.mongs.wear.domain.management.repository.SlotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetCurrentSlotUseCase @Inject constructor(
    private val mqttClient: MqttClient,
    private val managementRepository: ManagementRepository,
    private val slotRepository: SlotRepository,
) : BaseParamUseCase<SetCurrentSlotUseCase.Param, Unit>() {

    /**
     * 현재 몽 설정 UseCase
     * @throws DisSubMqttException
     * @throws SubMqttException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            mqttClient.disSubManager()
            managementRepository.updateMong(mongId = param.mongId)
            slotRepository.setCurrentSlot(mongId = param.mongId)
            mqttClient.subManager(mongId = param.mongId)
        }
    }

    data class Param(
        val mongId: Long,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is DisSubMqttException -> throw SetCurrentSlotUseCaseException()

            is SubMqttException -> throw SetCurrentSlotUseCaseException()

            else -> throw SetCurrentSlotUseCaseException()
        }
    }
}