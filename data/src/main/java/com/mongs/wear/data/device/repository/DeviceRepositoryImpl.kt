package com.mongs.wear.data.device.repository

import androidx.lifecycle.LiveData
import com.mongs.wear.data.device.api.DeviceApi
import com.mongs.wear.data.device.datastore.DeviceDataStore
import com.mongs.wear.data.device.dto.request.CreateDeviceRequestDto
import com.mongs.wear.data.device.dto.request.ExchangeWalkingCountRequestDto
import com.mongs.wear.data.device.dto.request.UpdateWalkingCountRequestDto
import com.mongs.wear.data.device.exception.CreateDeviceException
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
     * 기기 등록
     */
    override suspend fun createDevice(deviceId: String, totalWalkingCount: Int, deviceBootedDt: LocalDateTime, fcmToken: String) {

        val response = deviceApi.createDevice(
            createDeviceRequestDto = CreateDeviceRequestDto(
                deviceId = deviceId,
                totalWalkingCount = totalWalkingCount,
                deviceBootedDt = deviceBootedDt,
                fcmToken = fcmToken,
            )
        )

        if (!response.isSuccessful) {
            throw CreateDeviceException()
        }
    }

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
    override suspend fun updateWalkingCountInServer(deviceId: String, totalWalkingCount: Int, deviceBootedDt: LocalDateTime) {

        deviceDataStore.setTotalWalkingCount(totalWalkingCount = totalWalkingCount)

        val response = deviceApi.updateWalkingCount(
            UpdateWalkingCountRequestDto(
                deviceId = deviceId,
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

    override suspend fun setDeviceId(deviceId: String) {

        if (deviceId.isEmpty() || "unknown" == deviceId) {
            deviceDataStore.setDeviceId(deviceId = UUID.randomUUID().toString().replace("-", ""))
            return
        }

        deviceDataStore.setDeviceId(deviceId = deviceId)
    }

    override suspend fun getDeviceId(): String = deviceDataStore.getDeviceId()

    override suspend fun setBgMapTypeCode(mapTypeCode: String) {
        deviceDataStore.setBgMapTypeCode(mapTypeCode = mapTypeCode)
    }

    override suspend fun getBgMapTypeCodeLive(): LiveData<String> {
        return deviceDataStore.getBgMapTypeCodeLive()
    }

    override suspend fun setNetwork(network: Boolean) {
        deviceDataStore.setNetwork(network = network)
    }

    override suspend fun getNetworkLive(): LiveData<Boolean> {
        return deviceDataStore.getNetworkLive()
    }
}