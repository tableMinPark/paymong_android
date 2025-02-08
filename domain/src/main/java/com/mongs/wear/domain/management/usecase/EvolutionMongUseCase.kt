package com.mongs.wear.domain.management.usecase

import com.mongs.wear.core.exception.data.EvolutionMongException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.EvolutionMongUseCaseException
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.domain.management.repository.ManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EvolutionMongUseCase @Inject constructor(
    private val managementRepository: ManagementRepository,
) : BaseParamUseCase<EvolutionMongUseCase.Param, Unit>() {

    /**
     * 몽 진화 UseCase
     * @throws EvolutionMongException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            managementRepository.evolutionMong(mongId = param.mongId)
        }
    }

    data class Param(
        val mongId: Long,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is EvolutionMongException -> throw EvolutionMongUseCaseException()

            else -> throw EvolutionMongUseCaseException()
        }
    }
}