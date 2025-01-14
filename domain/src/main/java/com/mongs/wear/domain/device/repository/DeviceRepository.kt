package com.mongs.wear.domain.device.repository

import androidx.lifecycle.LiveData
import java.time.LocalDateTime

interface DeviceRepository {

    /**
     * 기기 ID 설정
     */
    suspend fun setDeviceId(deviceId: String)
    /**
     * deviceId 조회
     */
    suspend fun getDeviceId(): String

    /**
     * 기기 부팅 시간 설정
     */
    suspend fun setDeviceBootedDt(deviceBootedDt: LocalDateTime)

    /**
     * 기기 부팅 시간 조회
     */
    suspend fun getDeviceBootedDt(): LocalDateTime

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