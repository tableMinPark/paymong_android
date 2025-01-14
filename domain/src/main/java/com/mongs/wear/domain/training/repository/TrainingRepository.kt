package com.mongs.wear.domain.training.repository

interface TrainingRepository {

    /**
     * 훈련 달리기 완료
     */
    suspend fun trainingRunner(mongId: Long, score: Int)
}