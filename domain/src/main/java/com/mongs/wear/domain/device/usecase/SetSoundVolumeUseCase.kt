package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.SetSoundVolumeUseCaseException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.core.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetSoundVolumeUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseParamUseCase<SetSoundVolumeUseCase.Param, Unit>() {

    /**
     * 볼륨 설정 UseCase
     */
    override suspend fun execute(param: Param) {
        return withContext(Dispatchers.IO) {
            deviceRepository.setSoundVolume(soundVolume = param.soundVolume)
        }
    }

    data class Param(
        val soundVolume: Float
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            else -> throw SetSoundVolumeUseCaseException()
        }
    }
}