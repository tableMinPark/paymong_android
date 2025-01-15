package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.device.exception.ExchangeWalkingCountException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class ExchangeWalkingCountUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseParamUseCase<ExchangeWalkingCountUseCase.Param, Unit>() {

    override suspend fun execute(param: Param) {

        withContext(Dispatchers.IO) {

            deviceRepository.exchangeWalkingCount(
                mongId = param.mongId,
                walkingCount = param.walkingCount,
                deviceBootedDt = param.deviceBootedDt,
            )
        }
    }

    data class Param(

        val mongId: Long,

        val walkingCount: Int,

        val deviceBootedDt: LocalDateTime,
    )

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw ExchangeWalkingCountException()
        }
    }
}