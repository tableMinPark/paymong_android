package com.mongs.wear.domain.management.usecase

import com.mongs.wear.core.exception.data.CreateMongException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.core.exception.usecase.CreateMongUseCaseException
import com.mongs.wear.domain.management.repository.ManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateMongUseCase @Inject constructor(
    private val managementRepository: ManagementRepository,
) : BaseParamUseCase<CreateMongUseCase.Param, Unit>() {

    /**
     * 몽 생성 UseCase
     * @throws CreateMongException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            managementRepository.createMong(
                name = param.name,
                sleepStart = param.sleepStart,
                sleepEnd = param.sleepEnd,
            )
        }
    }

    data class Param(

        val name: String,

        val sleepStart: String,

        val sleepEnd: String
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is CreateMongException -> throw CreateMongUseCaseException()

            else -> throw CreateMongUseCaseException()
        }
    }
}