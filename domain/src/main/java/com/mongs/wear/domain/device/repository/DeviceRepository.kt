package com.mongs.wear.domain.device.repository

import androidx.lifecycle.LiveData
import java.time.LocalDateTime

interface DeviceRepository {

    /**
     * 플레이어 걸음 수 환전
     */
    suspend fun exchangeWalkingCount(mongId: Long, walkingCount: Int, deviceBootedDt: LocalDateTime)

    /**
     * 기기 총 걸음 수 서버 동기화
     */
    suspend fun updateWalkingCountInServer(totalWalkingCount: Int, deviceBootedDt: LocalDateTime)

    /**
     * 기기 총 걸음 수 동기화
     */
    suspend fun updateWalkingCountInLocal(totalWalkingCount: Int)

    /**
     * 플레이어 걸음 수 라이브 객체 조회
     */
    suspend fun getStepsLive(): LiveData<Int>

    /**
     * 기기 ID 설정
     */
    suspend fun setDeviceId(deviceId: String)

    /**
     * deviceId 조회
     */
    suspend fun getDeviceId(): String

    /**
     * 배경 화면 맵 타입 코드 등록
     */
    suspend fun setBgMapTypeCode(mapTypeCode: String)

    /**
     * 배경 화면 맵 타입 코드 라이브 객체 조회
     */
    suspend fun getBgMapTypeCodeLive(): LiveData<String>

    /**
     * 네트워크 플래그 설정
     */
    suspend fun setNetwork(network: Boolean)

    /**
     * 네트워크 플래그 라이브 객체 조회
     */
    suspend fun getNetworkLive(): LiveData<Boolean>
}