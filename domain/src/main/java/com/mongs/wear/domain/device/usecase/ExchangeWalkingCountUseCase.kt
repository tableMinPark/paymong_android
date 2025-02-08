package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.data.ExchangeWalkingException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.ExchangeWalkingCountUseCaseException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.core.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class ExchangeWalkingCountUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseParamUseCase<ExchangeWalkingCountUseCase.Param, Unit>() {

    /**
     * 걸음 수 환전 UseCase
     * @throws ExchangeWalkingException
     */
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

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is ExchangeWalkingException -> throw ExchangeWalkingCountUseCaseException()

            else -> throw ExchangeWalkingCountUseCaseException()
        }
    }
}