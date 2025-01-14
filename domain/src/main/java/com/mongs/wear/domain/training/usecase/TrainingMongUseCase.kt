package com.mongs.wear.domain.training.usecase

import com.mongs.wear.core.enums.TrainingCode
import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import com.mongs.wear.domain.training.exception.TrainingMongException
import com.mongs.wear.domain.training.repository.TrainingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrainingMongUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository,
) : BaseParamUseCase<TrainingMongUseCase.Param, Unit>() {

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

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw TrainingMongException()
        }
    }
}