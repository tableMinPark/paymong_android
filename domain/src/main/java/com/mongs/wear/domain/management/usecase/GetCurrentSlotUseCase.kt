package com.mongs.wear.domain.management.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.domain.global.usecase.BaseNoParamUseCase
import com.mongs.wear.domain.management.exception.GetCurrentSlotException
import com.mongs.wear.domain.management.repository.SlotRepository
import com.mongs.wear.domain.management.vo.MongVo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCurrentSlotUseCase @Inject constructor(
    private val mqttClient: MqttClient,
    private val slotRepository: SlotRepository,
) : BaseNoParamUseCase<LiveData<MongVo?>>() {

    override suspend fun execute(): LiveData<MongVo?> {

        return withContext(Dispatchers.IO) {

//            slotRepository.updateCurrentSlot()

            slotRepository.getCurrentSlot()?.let { mongModel ->
                mqttClient.subManager(mongId = mongModel.mongId)
            }

            slotRepository.getCurrentSlotLive().map { it?.toMongVo() }
        }
    }

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw GetCurrentSlotException()
        }
    }
}