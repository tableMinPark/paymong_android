package com.mongs.wear.domain.management.usecase

import com.mongs.wear.core.exception.data.StrokeMongException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.StrokeMongUseCaseException
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.domain.management.repository.ManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StrokeMongUseCase @Inject constructor(
    private val managementRepository: ManagementRepository,
) : BaseParamUseCase<StrokeMongUseCase.Param, Unit>() {

    /**
     * 몽 쓰다 듬기 UseCase
     * @throws StrokeMongException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            managementRepository.strokeMong(mongId = param.mongId)
        }
    }

    data class Param(
        val mongId: Long,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is StrokeMongException -> {

                val expirationSeconds = exception.result["expirationSeconds"]?.toString()
                    ?.toDouble()?.toLong()
                    ?: 0

                throw StrokeMongUseCaseException(expirationSeconds = expirationSeconds)
            }

            else -> throw StrokeMongUseCaseException()
        }
    }
}