package com.mongs.wear.presentation.global.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mongs.wear.core.utils.TimeUtil
import com.mongs.wear.domain.device.usecase.UpdateTotalWalkingCountUseCase
import com.mongs.wear.presentation.global.manager.StepSensorManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class StepSensorWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val updateTotalWalkingCountUseCase: UpdateTotalWalkingCountUseCase,
    private val stepSensorManager: StepSensorManager,
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val TAG = "StepsWorker"
        const val WORKER_NAME = "MONGS_STEPS_SYNC_WORKER"
    }

    override suspend fun doWork(): Result = try {

        val deviceBootedDt = TimeUtil.getBootedDt()

        val totalWalkingCount = stepSensorManager.getWalkingCount()

        Log.i(TAG, "[Worker] 총 걸음 수 : $totalWalkingCount")

        updateTotalWalkingCountUseCase(
            UpdateTotalWalkingCountUseCase.Param(
                deviceBootedDt = deviceBootedDt,
                totalWalkingCount = totalWalkingCount,
            )
        )

        Result.success()
    } catch (e: Exception) {
        Result.failure()
    }
}
