package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.SetNetworkUseCaseException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.core.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetNetworkUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseParamUseCase<SetNetworkUseCase.Param, Unit>() {

    /**
     * 네트워크 플래그 설정 UseCase
     */
    override suspend fun execute(param: Param) {
        return withContext(Dispatchers.IO) {
            deviceRepository.setNetwork(network = param.network)
        }
    }

    data class Param(
        val network: Boolean
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            else -> throw SetNetworkUseCaseException()
        }
    }
}