package com.mongs.wear.domain.player.repository

import androidx.lifecycle.LiveData
import java.time.LocalDateTime

interface PlayerRepository {

    /**
     * 플레이어 정보 갱신
     */
    suspend fun updatePlayer()

    /**
     * 플레이어 스타 포인트 라이브 객체 조회
     */
    suspend fun getStarPointLive(): LiveData<Int>

    /**
     * 플레이어 슬롯 갯수 조회
     */
    suspend fun getSlotCount(): Int

    /**
     * 플레이어 슬롯 구매
     */
    suspend fun buySlot()

    /**
     * 플레이어 스타 포인트 환전
     */
    suspend fun exchangeStarPoint(mongId: Long, starPoint: Int)

    /**
     * 플레이어 걸음 수 환전
     */
    suspend fun exchangeWalkingCount(mongId: Long, walkingCount: Int, deviceBootedDt: LocalDateTime)

    /**
     * 기기 총 걸음 수 서버 동기화
     */
    suspend fun syncTotalWalkingCount(deviceId: String, totalWalkingCount: Int, deviceBootedDt: LocalDateTime)

    /**
     * 플레이어 걸음 수 라이브 객체 조회
     */
    suspend fun getStepsLive(): LiveData<Int>

    /**
     * 기기 총 걸음 수 동기화
     */
    suspend fun setTotalWalkingCount(totalWalkingCount: Int)
}