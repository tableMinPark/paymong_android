package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.device.exception.CreateDeviceException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class CreateDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseParamUseCase<CreateDeviceUseCase.Param, Unit>() {

    override suspend fun execute(param: Param) {

        withContext(Dispatchers.IO) {

            deviceRepository.setDeviceId(deviceId = param.deviceId)

            deviceRepository.createDevice(
                deviceId = param.deviceId,
                deviceBootedDt = param.deviceBootedDt,
                totalWalkingCount = param.totalWalkingCount,
                fcmToken = param.fcmToken,
            )
        }
    }

    data class Param(

        val deviceId: String,

        val totalWalkingCount: Int,

        val deviceBootedDt: LocalDateTime,

        val fcmToken: String,
    )

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception) {
            else -> throw CreateDeviceException()
        }
    }
}