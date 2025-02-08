package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.SetNotificationUseCaseException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.core.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetNotificationUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseParamUseCase<SetNotificationUseCase.Param, Unit>() {

    /**
     * 알림 플래그 설정 UseCase
     */
    override suspend fun execute(param: Param) {

        return withContext(Dispatchers.IO) {
            deviceRepository.setNotification(notification = param.notification)
        }
    }

    data class Param(
        val notification: Boolean
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            else -> throw SetNotificationUseCaseException()
        }
    }
}