package com.mongs.wear.domain.player.repository

import androidx.lifecycle.LiveData

interface PlayerRepository {

    /**
     * 플레이어 정보 갱신
     * @throws GetPlayerException
     */
    suspend fun updatePlayer()

    /**
     * 플레이어 정보 등록
     * @throws CreatePlayerException
     */
    suspend fun createPlayer()

    /**
     * 플레이어 스타 포인트 라이브 객체 조회
     */
    suspend fun getStarPointLive(): LiveData<Int>

    /**
     * 플레이어 슬롯 갯수 조회
     * @throws GetPlayerException
     */
    suspend fun getSlotCount(): Int

    /**
     * 플레이어 슬롯 구매
     * @throws BuySlotException
     */
    suspend fun buySlot()

    /**
     * 플레이어 스타 포인트 환전
     * @throws ExchangeStarPointException
     */
    suspend fun exchangeStarPoint(mongId: Long, starPoint: Int)
}