package com.mongs.wear.domain.battle.repository

import androidx.lifecycle.LiveData
import com.mongs.wear.core.enums.MatchRoundCode
import com.mongs.wear.domain.battle.model.MatchModel
import com.mongs.wear.domain.battle.model.MatchPlayerModel

interface BattleRepository {

    /**
     * 매칭 대기열 등록
     */
    suspend fun createWaitMatching(mongId: Long)

    /**
     * 매칭 대기열 삭제
     */
    suspend fun deleteWaitMatching(mongId: Long)

    /**
     * 배틀룸 로컬 생성
     */
    suspend fun createMatch(deviceId: String): LiveData<MatchModel?>

    /**
     * 배틀룸 로컬 삭제
     */
    suspend fun deleteMatch()

    /**
     * 매칭 입장
     */
    suspend fun enterMatch(roomId: Long)

    /**
     * 매치 라이브 객체 조회
     */
    suspend fun getMatchLive(deviceId: String): LiveData<MatchModel>

    /**
     * 나의 매치 플레이어 라이브 객체 조회
     */
    suspend fun getMyMatchPlayerLive(): LiveData<MatchPlayerModel>

    /**
     * 상대방 매치 플레이어 라이브 객체 조회
     */
    suspend fun getRiverMatchPlayerLive(): LiveData<MatchPlayerModel>

    /**
     * 매치 시작
     */
    suspend fun startMatch(roomId: Long)



    suspend fun updateOverMatch(roomId: Long)

    suspend fun pickMatch(roomId: Long, targetPlayerId: String, pickCode: MatchRoundCode)

    suspend fun exitMatch(roomId: Long)
}