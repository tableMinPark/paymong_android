package com.mongs.wear.domain.device.usecase

import androidx.lifecycle.LiveData
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.GetBackgroundMapCodeUseCaseException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetBackgroundMapCodeUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : BaseNoParamUseCase<LiveData<String>>() {

    /**
     * 배경 코드 조회 UseCase
     */
    override suspend fun execute(): LiveData<String> {
        return withContext(Dispatchers.IO) {
            deviceRepository.getBgMapTypeCodeLive()
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            else -> throw GetBackgroundMapCodeUseCaseException()
        }
    }
}