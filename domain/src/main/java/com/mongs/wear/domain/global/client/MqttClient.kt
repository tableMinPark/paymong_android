package com.mongs.wear.domain.global.client

import com.mongs.wear.core.enums.MatchRoundCode

interface MqttClient {

    /**
     * 연결 여부 플래그 조회
     */
    suspend fun isConnected(): Boolean

    /**
     * 구독 준비 (콜백 클래스 등록 및 MqttClient 객체 생성)
     */
    suspend fun connect()

    /**
     * 구독중인 topic 전체 구독 해제
     * 구독 정보 유지 (mongId, accountId, roomId)
     */
    suspend fun pauseConnect()

    /**
     * 구독중인 topic 전체 구독 해제
     * 구독 정보 삭제 (mongId, accountId, roomId)
     */
    suspend fun disConnect()

    /**
     * Manager 구독
     */
    suspend fun subManager(mongId :Long)

    /**
     * Manager 구독 해제
     * 구독 정보 삭제 (mongId)
     */
    suspend fun disSubManager()

    /**
     * SearchMatch 구독
     */
    suspend fun subSearchMatch(deviceId: String)

    /**
     * SearchMatch 구독 해제
     * 구독 정보 삭제 (deviceId)
     */
    suspend fun disSubSearchMatch()

    /**
     * BattleMatch 구독
     */
    suspend fun subBattleMatch(roomId: Long)

    /**
     * BattleMatch 입장
     */
    suspend fun pubBattleMatchEnter(roomId: Long, playerId: String)

    /**
     * BattleMatch 선택
     */
    suspend fun pubBattleMatchPick(roomId: Long, playerId: String, targetPlayerId: String, pickCode: MatchRoundCode)

    /**
     * BattleMatch 퇴장
     */
    suspend fun pubBattleMatchExit(roomId: Long, playerId: String)

    /**
     * BattleMatch 구독 해제
     * 구독 정보 삭제 (roomId)
     */
    suspend fun disSubBattleMatch()

    /**
     * Player 구독
     */
    suspend fun subPlayer(accountId: Long)

    /**
     * Player 구독 해제
     * 구독 정보 삭제 (accountId)
     */
    suspend fun disSubPlayer()
}