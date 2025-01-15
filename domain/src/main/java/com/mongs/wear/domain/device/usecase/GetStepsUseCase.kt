package com.mongs.wear.domain.device.usecase

import androidx.lifecycle.LiveData
import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.usecase.BaseNoParamUseCase
import com.mongs.wear.domain.device.exception.GetStepsException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetStepsUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseNoParamUseCase<LiveData<Int>>() {

    override suspend fun execute(): LiveData<Int> {

        return withContext(Dispatchers.IO) {
            deviceRepository.getStepsLive()
        }
    }

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw GetStepsException()
        }
    }
}