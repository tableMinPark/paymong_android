package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.device.exception.SetSoundVolumeException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetSoundVolumeUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseParamUseCase<SetSoundVolumeUseCase.Param, Unit>() {

    override suspend fun execute(param: Param) {

        return withContext(Dispatchers.IO) {
            deviceRepository.setSoundVolume(soundVolume = param.soundVolume)
        }
    }

    data class Param(
        val soundVolume: Float
    )

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw SetSoundVolumeException()
        }
    }
}