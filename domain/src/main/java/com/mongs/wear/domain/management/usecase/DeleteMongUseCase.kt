package com.mongs.wear.domain.management.usecase

import com.mongs.wear.core.exception.data.DeleteMongException
import com.mongs.wear.core.exception.data.DisSubMqttException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.DeleteMongUseCaseException
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.domain.management.repository.ManagementRepository
import com.mongs.wear.domain.management.repository.SlotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteMongUseCase @Inject constructor(
    private val mqttClient: MqttClient,
    private val managementRepository: ManagementRepository,
    private val slotRepository: SlotRepository,
) : BaseParamUseCase<DeleteMongUseCase.Param, Unit>() {

    /**
     * 몽 삭제 UseCase
     * @throws DisSubMqttException
     * @throws DeleteMongException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            // 현재 선택된 몽인 경우 구독 해제
            slotRepository.getCurrentSlot()?.let { mongModel ->
                if (mongModel.mongId == param.mongId) {
                    mqttClient.disSubManager()
                }
            }

            managementRepository.deleteMong(mongId = param.mongId)
        }
    }

    data class Param(
        val mongId: Long,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is DisSubMqttException -> throw DeleteMongUseCaseException()

            is DeleteMongException -> throw DeleteMongUseCaseException()

            else -> throw DeleteMongUseCaseException()
        }
    }
}