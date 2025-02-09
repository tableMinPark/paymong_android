package com.mongs.wear.presentation.global.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.mongs.wear.core.utils.TimeUtil
import com.mongs.wear.domain.auth.repository.AuthRepository
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.presentation.global.manager.StepSensorManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class StepSensorWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val authRepository: AuthRepository,
    private val deviceRepository: DeviceRepository,
    private val stepSensorManager: StepSensorManager,
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val TAG = "StepSensorWorker"
        const val WORKER_NAME = "MONGS_STEPS_SYNC_WORKER"
    }

    override suspend fun doWork(): Result = try {

        // 로그인 여부 확인
        if (authRepository.isLogin()) {
            val deviceBootedDt = TimeUtil.getBootedDt()
            val totalWalkingCount = stepSensorManager.getWalkingCount()

            Log.i(TAG, "[Worker] 총 걸음 수 : $totalWalkingCount")

            deviceRepository.updateWalkingCountInServer(
                totalWalkingCount = totalWalkingCount,
                deviceBootedDt = deviceBootedDt,
            )
        } else {
            Log.i(TAG, "[Worker] 로그인이 되어있지 않음.")
        }

        Result.success()
    } catch (e: Exception) {
        Result.failure()
    }
}
