package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.device.exception.SetTotalWalkingCountException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetTotalWalkingCountUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseParamUseCase<SetTotalWalkingCountUseCase.Param, Unit>() {

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
            else -> throw SetTotalWalkingCountException()
        }
    }
}