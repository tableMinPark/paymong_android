package com.mongs.wear.presentation.global.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mongs.wear.domain.auth.repository.AuthRepository
import com.mongs.wear.domain.collection.repository.CollectionRepository
import com.mongs.wear.domain.collection.usecase.CreateMapCollectionUseCase
import com.mongs.wear.presentation.global.manager.LocationSensorManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class LocationSensorWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val authRepository: AuthRepository,
    private val createMapCollectionUseCase: CreateMapCollectionUseCase,
    private val locationSensorManager: LocationSensorManager,
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val TAG = "LocationSensorWorker"
        const val WORKER_NAME = "MONGS_LOCATION_SYNC_WORKER"
    }

    override suspend fun doWork(): Result = try {
        // 로그인 여부 확인
        if (authRepository.isLogin()) {
            locationSensorManager.getLocation()?.let { locationVo ->

                Log.i(TAG, "[Worker] GEO : $locationVo")

                createMapCollectionUseCase(
                    CreateMapCollectionUseCase.Param(
                        latitude = locationVo.latitude,
                        longitude = locationVo.longitude,
                    )
                )
            }
        } else {
            Log.i(TAG, "[Worker] 로그인이 되어있지 않음.")
        }

        Result.success()
    } catch (e: Exception) {
        Result.failure()
    }
}
