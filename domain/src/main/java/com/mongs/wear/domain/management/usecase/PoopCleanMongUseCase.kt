package com.mongs.wear.domain.management.usecase

import com.mongs.wear.core.exception.data.PoopCleanMongException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.PoopCleanMongUseCaseException
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.domain.management.repository.ManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PoopCleanMongUseCase @Inject constructor(
    private val managementRepository: ManagementRepository,
) : BaseParamUseCase<PoopCleanMongUseCase.Param, Unit>() {

    /**
     * 몽 배변 처리 UseCase
     * @throws PoopCleanMongException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            managementRepository.poopCleanMong(mongId = param.mongId)
        }
    }

    data class Param(
        val mongId: Long,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is PoopCleanMongException -> throw PoopCleanMongUseCaseException()

            else -> throw PoopCleanMongUseCaseException()
        }
    }
}