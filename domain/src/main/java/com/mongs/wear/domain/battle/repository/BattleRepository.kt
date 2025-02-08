package com.mongs.wear.domain.battle.repository

import androidx.lifecycle.LiveData
import com.mongs.wear.core.enums.MatchRoundCode
import com.mongs.wear.domain.battle.model.BattleRewardModel
import com.mongs.wear.domain.battle.model.MatchModel
import com.mongs.wear.domain.battle.model.MatchPlayerModel

interface BattleRepository {

    /**
     * 배틀 정보 갱신
     * @throws GetBattleException
     */
    suspend fun updateMatch(roomId: Long)

    /**
     * 배틀 보상 정보 조회
     * @throws GetBattleRewardException
     */
    suspend fun getBattleReward(): BattleRewardModel

    /**
     * 매칭 대기열 등록
     * @throws CreateMatchException
     */
    suspend fun createWaitMatching(mongId: Long)

    /**
     * 매칭 대기열 삭제
     * @throws DeleteMatchException
     */
    suspend fun deleteWaitMatching(mongId: Long)

    /**
     * 배틀룸 로컬 등록
     */
    suspend fun createMatch(deviceId: String): LiveData<MatchModel?>

    /**
     * 배틀룸 로컬 삭제
     */
    suspend fun deleteMatch()

    /**
     * 매칭 입장
     * @throws EnterMatchException
     */
    suspend fun enterMatch(roomId: Long)

    /**
     * 매치 조회
     */
    suspend fun getMatch(deviceId: String): MatchModel?

    /**
     * 매치 라이브 객체 조회
     * @throws NotExistsMatchException
     */
    suspend fun getMatchLive(deviceId: String): LiveData<MatchModel>

    /**
     * 나의 매치 플레이어 라이브 객체 조회
     * @throws NotExistsMatchPlayerException
     */
    suspend fun getMyMatchPlayerLive(): LiveData<MatchPlayerModel>

    /**
     * 상대방 매치 플레이어 라이브 객체 조회
     * @throws NotExistsMatchPlayerException
     */
    suspend fun getRiverMatchPlayerLive(): LiveData<MatchPlayerModel>

    /**
     * 매치 시작
     */
    suspend fun startMatch(roomId: Long)

    /**
     * 매치 퇴장
     */
    suspend fun exitMatch(roomId: Long)

    /**
     * 매치 선택
     * @throws PickMatchException
     */
    suspend fun pickMatch(roomId: Long, targetPlayerId: String, pickCode: MatchRoundCode)

    /**
     * 매치 결과 정보 업데이트
     * @throws UpdateOverMatchException
     */
    suspend fun updateOverMatch(roomId: Long)
}