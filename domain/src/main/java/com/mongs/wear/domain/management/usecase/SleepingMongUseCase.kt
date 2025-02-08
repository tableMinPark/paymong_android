package com.mongs.wear.domain.management.usecase

import com.mongs.wear.core.exception.data.SleepMongException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.SleepingMongUseCaseException
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.domain.management.repository.ManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SleepingMongUseCase @Inject constructor(
    private val managementRepository: ManagementRepository,
) : BaseParamUseCase<SleepingMongUseCase.Param, Unit>() {

    /**
     * 몽 수면/기상 UseCase
     * @throws SleepMongException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            managementRepository.sleepingMong(mongId = param.mongId)
        }
    }

    data class Param(
        val mongId: Long,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is SleepMongException -> throw SleepingMongUseCaseException()

            else -> throw SleepingMongUseCaseException()
        }
    }
}