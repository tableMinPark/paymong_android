package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.device.exception.SetDeviceIdException
import com.mongs.wear.domain.device.exception.SetNetworkException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetDeviceIdUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseParamUseCase<SetDeviceIdUseCase.Param, Unit>() {

    override suspend fun execute(param: Param) {

        return withContext(Dispatchers.IO) {
            deviceRepository.setDeviceId(deviceId = param.deviceId)
        }
    }

    data class Param(
        val deviceId: String
    )

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw SetDeviceIdException()
        }
    }
}