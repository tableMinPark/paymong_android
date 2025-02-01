package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.device.exception.SetLocalTotalWalkingCountException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetLocalTotalWalkingCountUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseParamUseCase<SetLocalTotalWalkingCountUseCase.Param, Unit>() {

    override suspend fun execute(param: Param) {

        withContext(Dispatchers.IO) {
            deviceRepository.updateWalkingCountInLocal(totalWalkingCount = param.totalWalkingCount)
        }
    }

    data class Param(
        val totalWalkingCount: Int,
    )

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw SetLocalTotalWalkingCountException()
        }
    }
}