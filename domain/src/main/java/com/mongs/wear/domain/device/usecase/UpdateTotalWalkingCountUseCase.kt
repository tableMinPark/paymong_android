package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.device.exception.UpdateTotalWalkingCountException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class UpdateTotalWalkingCountUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseParamUseCase<UpdateTotalWalkingCountUseCase.Param, Unit>() {

    override suspend fun execute(param: Param) {

        withContext(Dispatchers.IO) {

            val deviceId = deviceRepository.getDeviceId()

            deviceRepository.updateWalkingCountInServer(
                deviceId = deviceId,
                totalWalkingCount = param.totalWalkingCount,
                deviceBootedDt = param.deviceBootedDt,
            )
        }
    }

    data class Param(

        val totalWalkingCount: Int,

        val deviceBootedDt: LocalDateTime,
    )

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw UpdateTotalWalkingCountException()
        }
    }
}