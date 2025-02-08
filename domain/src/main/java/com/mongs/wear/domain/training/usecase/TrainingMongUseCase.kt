package com.mongs.wear.domain.training.usecase

import com.mongs.wear.core.enums.TrainingCode
import com.mongs.wear.core.exception.data.TrainingRunnerException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.TrainingMongUseCaseException
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.domain.training.repository.TrainingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrainingMongUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository,
) : BaseParamUseCase<TrainingMongUseCase.Param, Unit>() {

    /**
     * 훈련 완료 UseCase
     * @throws TrainingRunnerException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            when(param.trainingCode) {
                // 훈련 달리기
                TrainingCode.RUNNER -> {
                    trainingRepository.trainingRunner(
                        mongId = param.mongId,
                        score = param.score,
                    )
                }

                // 훈련 농구
                TrainingCode.BASKETBALL -> {

                }
            }
        }
    }

    data class Param(

        val mongId: Long,

        val trainingCode: TrainingCode,

        val score: Int,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is TrainingRunnerException -> throw TrainingMongUseCaseException()

            else -> throw TrainingMongUseCaseException()
        }
    }
}