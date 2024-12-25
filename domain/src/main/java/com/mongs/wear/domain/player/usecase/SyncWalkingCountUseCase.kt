package com.mongs.wear.domain.player.usecase

import android.util.Log
import com.mongs.wear.domain.common.repository.AppRepository
import com.mongs.wear.domain.player.repository.PlayerRepository
import javax.inject.Inject

class SyncWalkingCountUseCase @Inject constructor(
    private val appRepository: AppRepository,
    private val playerRepository: PlayerRepository,
) {

    suspend operator fun invoke(totalWalkingCount: Int) : Boolean {
        try {

            val deviceId = appRepository.getDeviceId()

            val deviceBootedDt = appRepository.getDeviceBootedDt()

            playerRepository.syncWalkingCount(deviceId = deviceId, totalWalkingCount = totalWalkingCount, deviceBootedDt = deviceBootedDt)

            return true

        } catch (_: Exception) {

            return false
        }
    }
}