package com.mongs.wear.data.device.repository

import androidx.lifecycle.LiveData
import com.mongs.wear.data.device.api.DeviceApi
import com.mongs.wear.data.device.datastore.DeviceDataStore
import com.mongs.wear.data.device.dto.request.ExchangeWalkingCountRequestDto
import com.mongs.wear.data.device.dto.request.UpdateWalkingCountRequestDto
import com.mongs.wear.data.device.exception.ExchangeWalkingException
import com.mongs.wear.data.global.utils.HttpUtil
import com.mongs.wear.domain.device.repository.DeviceRepository
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepositoryImpl @Inject constructor(
    private val httpUtil: HttpUtil,
    private val deviceApi: DeviceApi,
    private val deviceDataStore: DeviceDataStore,
) : DeviceRepository {

    /**
     * 걸음 수 환전
     */
    override suspend fun exchangeWalkingCount(mongId: Long, walkingCount: Int, deviceBootedDt: LocalDateTime) {

        val totalWalkingCount = deviceDataStore.getTotalWalkingCount()

        val response = deviceApi.exchangeWalkingCount(
            exchangeWalkingCountRequestDto = ExchangeWalkingCountRequestDto(
                mongId = mongId,
                walkingCount = walkingCount,
                totalWalkingCount = totalWalkingCount,
                deviceBootedDt = deviceBootedDt,
            )
        )

        if (response.isSuccessful) {
            response.body()?.let { body ->
                deviceDataStore.setWalkingCount(walkingCount = body.result.walkingCount)
                deviceDataStore.setConsumeWalkingCount(consumeWalkingCount = body.result.consumeWalkingCount)
            }
        } else {
            throw ExchangeWalkingException(result = httpUtil.getErrorResult(response.errorBody()))
        }
    }

    /**
     * 걸음 수 서버 동기화
     */
    override suspend fun updateWalkingCountInServer(totalWalkingCount: Int, deviceBootedDt: LocalDateTime) {

        deviceDataStore.setTotalWalkingCount(totalWalkingCount = totalWalkingCount)

        val response = deviceApi.updateWalkingCount(
            UpdateWalkingCountRequestDto(
                totalWalkingCount = totalWalkingCount,
                deviceBootedDt = deviceBootedDt,
            )
        )

        if (response.isSuccessful) {
            response.body()?.let { body ->
                deviceDataStore.setWalkingCount(walkingCount = body.result.walkingCount)
                deviceDataStore.setConsumeWalkingCount(consumeWalkingCount = body.result.consumeWalkingCount)
            }
        }
    }

    /**
     * 걸음 수 조회
     */
    override suspend fun getStepsLive(): LiveData<Int> = deviceDataStore.getStepsLive()

    /**
     * 걸음 수 로컬 동기화
     */
    override suspend fun updateWalkingCountInLocal(totalWalkingCount: Int) {
        deviceDataStore.setTotalWalkingCount(totalWalkingCount = totalWalkingCount)
    }

    /**
     * 기기 ID 조회
     */
    override suspend fun setDeviceId(deviceId: String) {

        if (deviceId.isEmpty() || "unknown" == deviceId) {
            deviceDataStore.setDeviceId(deviceId = UUID.randomUUID().toString().replace("-", ""))
        } else {
            deviceDataStore.setDeviceId(deviceId = deviceId)
        }
    }

    /**
     * 기기 ID 조회
     */
    override suspend fun getDeviceId(): String = deviceDataStore.getDeviceId()

    /**
     * 배경 화면 설정
     */
    override suspend fun setBgMapTypeCode(mapTypeCode: String) {
        deviceDataStore.setBgMapTypeCode(mapTypeCode = mapTypeCode)
    }

    /**
     * 배경 화면 라이브 객체 조회
     */
    override suspend fun getBgMapTypeCodeLive(): LiveData<String> {
        return deviceDataStore.getBgMapTypeCodeLive()
    }

    /**
     * 네트 워크 flag 설정
     */
    override suspend fun setNetwork(network: Boolean) {
        deviceDataStore.setNetwork(network = network)
    }

    /**
     * 네트 워크 flag 라이브 객체 조회
     */
    override suspend fun getNetworkLive(): LiveData<Boolean> {
        return deviceDataStore.getNetworkLive()
    }

    /**
     * 알림 플래그 설정
     */
    override suspend fun setNotification(notification: Boolean) {
        deviceDataStore.setNotification(notification = notification)
    }

    /**
     * 알림 플래그 조회
     */
    override suspend fun getNotification(): Boolean {
        return deviceDataStore.getNotification()
    }

    /**
     * 알림 플래그 라이브 객체 조회
     */
    override suspend fun getNotificationLive(): LiveData<Boolean> {
        return deviceDataStore.getNotificationLive()
    }

    /**
     * 음량 설정
     */
    override suspend fun setSoundVolume(soundVolume: Float) {
        deviceDataStore.setSoundVolume(soundVolume = soundVolume)
    }

    /**
     * 음량 조회
     */
    override suspend fun getSoundVolume(): Float {
        return deviceDataStore.getSoundVolume()
    }
}