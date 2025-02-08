package com.mongs.wear.domain.training.usecase

import com.mongs.wear.core.enums.TrainingCode
import com.mongs.wear.core.exception.data.GetTrainingRunnerException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.core.exception.usecase.GetTrainingPayPointUseCaseException
import com.mongs.wear.domain.training.repository.TrainingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTrainingPayPointUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository,
) : BaseParamUseCase<GetTrainingPayPointUseCase.Param, Int>() {

    /**
     * 훈련 보상 정보 조회 UseCase
     * @throws GetTrainingRunnerException
     */
    override suspend fun execute(param: Param): Int {
        return withContext(Dispatchers.IO) {

            when(param.trainingCode) {
                TrainingCode.RUNNER -> {
                    trainingRepository.getTrainingRunner().payPoint
                }

                // 훈련 농구
                TrainingCode.BASKETBALL -> {
                    0
                }
            }
        }
    }

    data class Param(

        val trainingCode: TrainingCode,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is GetTrainingRunnerException -> throw GetTrainingPayPointUseCaseException()

            else -> throw GetTrainingPayPointUseCaseException()
        }
    }
}