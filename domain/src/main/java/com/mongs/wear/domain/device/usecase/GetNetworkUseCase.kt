package com.mongs.wear.domain.device.usecase

import androidx.lifecycle.LiveData
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.GetNetworkUseCaseException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetNetworkUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseNoParamUseCase<LiveData<Boolean>>() {

    /**
     * 네트워크 플래그 조회 UseCase
     */
    override suspend fun execute(): LiveData<Boolean> {
        return withContext(Dispatchers.IO) {
            deviceRepository.getNetworkLive()
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            else -> throw GetNetworkUseCaseException()
        }
    }
}