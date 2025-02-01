package com.mongs.wear.domain.training.usecase

import com.mongs.wear.core.enums.TrainingCode
import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import com.mongs.wear.domain.training.exception.GetTrainingPayPointException
import com.mongs.wear.domain.training.repository.TrainingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTrainingPayPointUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository,
) : BaseParamUseCase<GetTrainingPayPointUseCase.Param, Int>() {

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

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw GetTrainingPayPointException()
        }
    }
}