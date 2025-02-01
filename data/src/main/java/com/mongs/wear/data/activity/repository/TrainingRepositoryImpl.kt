package com.mongs.wear.data.activity.repository

import com.mongs.wear.data.activity.api.TrainingApi
import com.mongs.wear.data.activity.dto.request.TrainingRunnerRequestDto
import com.mongs.wear.data.activity.exception.GetTrainingRunnerException
import com.mongs.wear.data.activity.exception.TrainingRunnerException
import com.mongs.wear.data.global.utils.HttpUtil
import com.mongs.wear.domain.training.model.TrainingModel
import com.mongs.wear.domain.training.repository.TrainingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainingRepositoryImpl @Inject constructor(
    private val httpUtil: HttpUtil,
    private val trainingApi: TrainingApi,
) : TrainingRepository {

    /**
     * 훈련 달리기 보상 정보 조회
     */
    override suspend fun getTrainingRunner() : TrainingModel {

        val response = trainingApi.getTrainingRunner()

        if (response.isSuccessful) {
            response.body()?.let { body ->
                return TrainingModel(
                    payPoint = body.result.payPoint,
                )
            }
        }

        throw GetTrainingRunnerException(result = httpUtil.getErrorResult(response.errorBody()))
    }

    /**
     * 훈련 달리기 완료
     */
    override suspend fun trainingRunner(mongId: Long, score: Int) {

        val response = trainingApi.trainingRunner(mongId = mongId,
            trainingRunnerRequestDto = TrainingRunnerRequestDto(
                score = score,
            )
        )

        if (!response.isSuccessful) {
            throw TrainingRunnerException(result = httpUtil.getErrorResult(response.errorBody()))
        }
    }
}