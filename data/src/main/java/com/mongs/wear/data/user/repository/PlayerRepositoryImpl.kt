package com.mongs.wear.data.user.repository

import androidx.lifecycle.LiveData
import com.mongs.wear.data.global.utils.HttpUtil
import com.mongs.wear.data.user.api.PlayerApi
import com.mongs.wear.data.user.datastore.PlayerDataStore
import com.mongs.wear.data.user.dto.request.ExchangeStarPointRequestDto
import com.mongs.wear.data.user.exception.BuySlotException
import com.mongs.wear.data.user.exception.CreatePlayerException
import com.mongs.wear.data.user.exception.ExchangeStarPointException
import com.mongs.wear.data.user.exception.GetPlayerException
import com.mongs.wear.domain.player.repository.PlayerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepositoryImpl @Inject constructor(
    private val httpUtil: HttpUtil,
    private val playerApi: PlayerApi,
    private val playerDataStore: PlayerDataStore,
): PlayerRepository {

    /**
     * 플레이어 정보 갱신
     */
    override suspend fun updatePlayer() {

        val response = playerApi.getPlayer()

        if (response.isSuccessful) {
            response.body()?.let { body ->
                playerDataStore.setStarPoint(starPoint = body.result.starPoint)
            }
        } else {
            throw GetPlayerException(result = httpUtil.getErrorResult(response.errorBody()))
        }
    }

    /**
     * 플레이어 정보 등록
     */
    override suspend fun createPlayer() {

        val response = playerApi.createPlayer()

        if (!response.isSuccessful) {
            throw CreatePlayerException()
        }
    }

    /**
     * 스타 포인트 조회
     */
    override suspend fun getStarPointLive(): LiveData<Int> {
        return playerDataStore.getStarPointLive()
    }

    /**
     * 슬롯 카운트 조회
     */
    override suspend fun getSlotCount(): Int {

        val response = playerApi.getPlayer()

        if (response.isSuccessful) {
            response.body()?.let { body ->
                return body.result.slotCount
            } ?: run {
                return 1
            }

        } else {
            throw GetPlayerException(result = httpUtil.getErrorResult(response.errorBody()))
        }
    }

    /**
     * 슬롯 구매
     */
    override suspend fun buySlot() {

        val response = playerApi.buySlot()

        if (!response.isSuccessful) {
            throw BuySlotException(result = httpUtil.getErrorResult(response.errorBody()))
        }
    }

    /**
     * 스타 포인트 환전
     */
    override suspend fun exchangeStarPoint(mongId: Long, starPoint: Int) {

        val response = playerApi.exchangeStarPoint(
            exchangeStarPointRequestDto = ExchangeStarPointRequestDto(
                mongId = mongId,
                starPoint = starPoint,
            )
        )

        if (!response.isSuccessful) {
            throw ExchangeStarPointException(result = httpUtil.getErrorResult(response.errorBody()))
        }
    }
}