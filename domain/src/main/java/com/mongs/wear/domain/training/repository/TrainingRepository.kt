package com.mongs.wear.domain.training.repository

import com.mongs.wear.domain.training.model.TrainingModel

interface TrainingRepository {

    /**
     * 훈련 달리기 정보 조회
     */
    suspend fun getTrainingRunner(): TrainingModel

    /**
     * 훈련 달리기 완료
     */
    suspend fun trainingRunner(mongId: Long, score: Int)
}