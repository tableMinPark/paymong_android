package com.mongs.wear.domain.management.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import com.mongs.wear.domain.management.exception.SetCurrentSlotException
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

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw SetCurrentSlotException()
        }
    }
}