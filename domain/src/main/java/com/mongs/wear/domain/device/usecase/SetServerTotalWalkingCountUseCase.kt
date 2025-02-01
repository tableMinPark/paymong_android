package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.core.utils.TimeUtil
import com.mongs.wear.domain.auth.repository.AuthRepository
import com.mongs.wear.domain.device.exception.SetServerTotalWalkingCountException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetServerTotalWalkingCountUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val deviceRepository: DeviceRepository,
) : BaseParamUseCase<SetServerTotalWalkingCountUseCase.Param, Unit>() {

    override suspend fun execute(param: Param) {

        withContext(Dispatchers.IO) {
            if (authRepository.isLogin()) {
                val deviceBootedDt = TimeUtil.getBootedDt()

                deviceRepository.updateWalkingCountInServer(
                    totalWalkingCount = param.totalWalkingCount,
                    deviceBootedDt = deviceBootedDt,
                )
            }
        }
    }

    data class Param(
        val totalWalkingCount: Int,
    )

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        throw when (exception.code) {
            else -> SetServerTotalWalkingCountException()
        }
    }
}