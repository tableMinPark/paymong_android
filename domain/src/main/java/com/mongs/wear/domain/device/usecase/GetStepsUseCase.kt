package com.mongs.wear.domain.device.usecase

import androidx.lifecycle.LiveData
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import com.mongs.wear.core.exception.usecase.GetStepsUseCaseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetStepsUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseNoParamUseCase<LiveData<Int>>() {

    /**
     * 걸음 수 조회 UseCase
     */
    override suspend fun execute(): LiveData<Int> {
        return withContext(Dispatchers.IO) {
            deviceRepository.getStepsLive()
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            else -> throw GetStepsUseCaseException()
        }
    }
}