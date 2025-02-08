package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.SetLocalTotalWalkingCountUseCaseException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.core.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetLocalTotalWalkingCountUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseParamUseCase<SetLocalTotalWalkingCountUseCase.Param, Unit>() {

    /**
     * 총 걸음 수 로컬 저장 UseCase
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            deviceRepository.updateWalkingCountInLocal(totalWalkingCount = param.totalWalkingCount)
        }
    }

    data class Param(
        val totalWalkingCount: Int,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            else -> throw SetLocalTotalWalkingCountUseCaseException()
        }
    }
}