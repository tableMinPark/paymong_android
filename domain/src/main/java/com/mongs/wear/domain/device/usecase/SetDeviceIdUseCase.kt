package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.SetDeviceIdUseCaseException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.core.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetDeviceIdUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseParamUseCase<SetDeviceIdUseCase.Param, Unit>() {

    /**
     * 기기 ID 설정 UseCase
     */
    override suspend fun execute(param: Param) {
        return withContext(Dispatchers.IO) {
            deviceRepository.setDeviceId(deviceId = param.deviceId)
        }
    }

    data class Param(
        val deviceId: String
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            else -> throw SetDeviceIdUseCaseException()
        }
    }
}