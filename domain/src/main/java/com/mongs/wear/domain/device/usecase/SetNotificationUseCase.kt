package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.device.exception.SetNotificationException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetNotificationUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseParamUseCase<SetNotificationUseCase.Param, Unit>() {

    override suspend fun execute(param: Param) {

        return withContext(Dispatchers.IO) {
            deviceRepository.setNotification(notification = param.notification)
        }
    }

    data class Param(
        val notification: Boolean
    )

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw SetNotificationException()
        }
    }
}