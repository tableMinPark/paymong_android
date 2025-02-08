package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.GetSoundVolumeUseCaseException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSoundVolumeUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseNoParamUseCase<Float>() {

    /**
     * 볼륨 조회 UseCase
     */
    override suspend fun execute(): Float {
        return withContext(Dispatchers.IO) {
            deviceRepository.getSoundVolume()
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            else -> throw GetSoundVolumeUseCaseException()
        }
    }
}