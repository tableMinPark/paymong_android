package com.mongs.wear.domain.global.client

import com.mongs.wear.core.enums.MatchRoundCode
import com.mongs.wear.core.exception.data.ConnectMqttException
import com.mongs.wear.core.exception.data.DisSubMqttException
import com.mongs.wear.core.exception.data.DisconnectMqttException
import com.mongs.wear.core.exception.data.PauseMqttException
import com.mongs.wear.core.exception.data.PubMqttException
import com.mongs.wear.core.exception.data.SubMqttException

interface MqttClient {

    /**
     * 연결 여부 플래그 조회
     */
    suspend fun isConnected(): Boolean

    /**
     * 브로커 연결
     * @throws ConnectMqttException
     */
    suspend fun connect()

    /**
     * 브로커 일시 중지
     * @throws PauseMqttException
     */
    suspend fun pauseConnect()

    /**
     * 브로커 연결 해제
     * @throws DisconnectMqttException
     */
    suspend fun disConnect()

    /**
     * 몽 매니저 구독
     * @throws SubMqttException
     */
    suspend fun subManager(mongId :Long)

    /**
     * 몽 매니저 구독 해제
     * @throws DisSubMqttException
     */
    suspend fun disSubManager()

    /**
     * 플레이어 구독
     * @throws SubMqttException
     */
    suspend fun subPlayer(accountId: Long)

    /**
     * 플레이어 구독 해제
     * @throws DisSubMqttException
     */
    suspend fun disSubPlayer()

    /**
     * 배틀 매칭 대기열 구독
     * @throws SubMqttException
     */
    suspend fun subSearchMatch(deviceId: String)

    /**
     * 배틀 매칭 대기열 구독 해제
     * @throws DisSubMqttException
     */
    suspend fun disSubSearchMatch()

    /**
     * 배틀 매치 구독
     * @throws SubMqttException
     */
    suspend fun subBattleMatch(roomId: Long)

    /**
     * 배틀 매치 구독 해제
     * @throws DisSubMqttException
     */
    suspend fun disSubBattleMatch()

    /**
     * 배틀 입장 메시지 전송
     * @throws PubMqttException
     */
    suspend fun pubBattleMatchEnter(roomId: Long, playerId: String)

    /**
     * 배틀 선택 메시지 전송
     * @throws PubMqttException
     */
    suspend fun pubBattleMatchPick(roomId: Long, playerId: String, targetPlayerId: String, pickCode: MatchRoundCode)

    /**
     * 배틀 퇴장 메시지 전송
     * @throws PubMqttException
     */
    suspend fun pubBattleMatchExit(roomId: Long, playerId: String)
}