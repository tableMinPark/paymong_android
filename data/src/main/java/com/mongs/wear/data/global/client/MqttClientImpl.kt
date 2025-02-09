package com.mongs.wear.data.global.client

import android.content.Context
import com.mongs.wear.core.enums.MatchRoundCode
import com.mongs.wear.core.exception.data.ConnectMqttException
import com.mongs.wear.core.exception.data.DisSubMqttException
import com.mongs.wear.core.exception.data.DisconnectMqttException
import com.mongs.wear.core.exception.data.PauseMqttException
import com.mongs.wear.core.exception.data.PubMqttException
import com.mongs.wear.core.exception.data.SubMqttException
import com.mongs.wear.data.R
import com.mongs.wear.data.activity.dto.request.EnterBattleRequestDto
import com.mongs.wear.data.activity.dto.request.ExitBattleRequestDto
import com.mongs.wear.data.activity.dto.request.PickBattleRequestDto
import com.mongs.wear.data.global.api.MqttApi
import com.mongs.wear.domain.global.client.MqttClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MqttClientImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mqttApi: MqttApi,
) : MqttClient {

    companion object {
        val mqttState = MqttState()
    }

    /**
     * 연결 여부 플래그 조회
     */
    override suspend fun isConnected(): Boolean {

        while (mqttState.connectPending) delay(500)

        return mqttApi.isConnected()
    }

    /**
     * 브로커 연결
     * @throws ConnectMqttException
     */
    override suspend fun connect() {

        if (mqttState.connectPending) return

        if (mqttState.isDisConnect() || mqttState.isPauseDisConnect()) {
            try {
                mqttState.connectPending = true

                mqttApi.connect()

                mqttState.broker = MqttState.MqttConnectedStateCode.CONNECT

                mqttState.connectPending = false

            } catch (e: Exception) {

                mqttState.connectPending = false

                throw ConnectMqttException()
            }
        }
    }

    /**
     * 브로커 일시 중지
     * @throws PauseMqttException
     */
    override suspend fun pauseConnect() {

        if (mqttState.isConnect()) {
            try {
                mqttState.broker = MqttState.MqttConnectedStateCode.PAUSE_DISCONNECT
                mqttApi.disConnect()

            } catch (_: Exception) {
                mqttState.broker = MqttState.MqttConnectedStateCode.CONNECT
                throw PauseMqttException()
            }
        }
    }

    /**
     * 브로커 연결 해제
     * @throws DisconnectMqttException
     */
    override suspend fun disConnect() {

        if (mqttState.isConnect()) {
            try {
                mqttApi.disConnect()
                mqttState.broker = MqttState.MqttConnectedStateCode.DISCONNECT
            } catch (_: Exception) {
                throw DisconnectMqttException()
            }
        }
    }

    /**
     * 몽 매니저 구독
     * @throws SubMqttException
     */
    override suspend fun subManager(mongId: Long) {

        if (mqttState.isConnect()) {
            try {
                if (mqttState.manager == MqttState.MqttSubscribeStateCode.DIS_SUB) {
                    mqttState.managerTopic =
                        "${context.getString(R.string.mqtt_manager_topic)}/$mongId"
                    mqttApi.subscribe(mqttState.managerTopic)
                    mqttState.manager = MqttState.MqttSubscribeStateCode.SUB
                }
            } catch (_: Exception) {
                throw SubMqttException()
            }
        }
    }

    /**
     * 몽 매니저 구독 해제
     * @throws DisSubMqttException
     */
    override suspend fun disSubManager() {

        if (mqttState.isConnect()) {
            try {
                if (mqttState.manager == MqttState.MqttSubscribeStateCode.SUB) {
                    mqttApi.disSubscribe(mqttState.managerTopic)
                    mqttState.managerTopic = ""
                    mqttState.manager = MqttState.MqttSubscribeStateCode.DIS_SUB
                }
            } catch (_: Exception) {
                throw DisSubMqttException()
            }
        }
    }

    /**
     * 플레이어 구독
     * @throws SubMqttException
     */
    override suspend fun subPlayer(accountId: Long) {

        if (mqttState.isConnect()) {
            try {
                if (mqttState.player == MqttState.MqttSubscribeStateCode.DIS_SUB) {
                    mqttState.playerTopic =
                        "${context.getString(R.string.mqtt_player_topic)}/$accountId"
                    mqttApi.subscribe(mqttState.playerTopic)
                    mqttState.player = MqttState.MqttSubscribeStateCode.SUB
                }
            } catch (_: Exception) {
                throw SubMqttException()
            }
        }
    }

    /**
     * 플레이어 구독 해제
     * @throws DisSubMqttException
     */
    override suspend fun disSubPlayer() {

        if (mqttState.isConnect()) {
            try {
                if (mqttState.player == MqttState.MqttSubscribeStateCode.SUB) {
                    mqttApi.disSubscribe(mqttState.playerTopic)
                    mqttState.playerTopic = ""
                    mqttState.player = MqttState.MqttSubscribeStateCode.DIS_SUB
                }
            } catch (_: Exception) {
                throw DisSubMqttException()
            }
        }
    }

    /**
     * 배틀 매칭 대기열 구독
     * @throws SubMqttException
     */
    override suspend fun subSearchMatch(deviceId: String) {

        if (mqttState.isConnect()) {
            try {
                if (mqttState.searchMatch == MqttState.MqttSubscribeStateCode.DIS_SUB) {
                    mqttState.searchMatchTopic =
                        "${context.getString(R.string.mqtt_battle_base_topic)}/search/$deviceId"
                    mqttApi.subscribe(mqttState.searchMatchTopic)
                    mqttState.searchMatch = MqttState.MqttSubscribeStateCode.SUB
                }
            } catch (_: Exception) {
                throw SubMqttException()
            }
        }
    }

    /**
     * 배틀 매칭 대기열 구독 해제
     * @throws DisSubMqttException
     */
    override suspend fun disSubSearchMatch() {

        if (mqttState.isConnect()) {
            try {
                if (mqttState.searchMatch == MqttState.MqttSubscribeStateCode.SUB) {
                    mqttApi.disSubscribe(mqttState.searchMatchTopic)
                    mqttState.searchMatchTopic = ""
                    mqttState.searchMatch = MqttState.MqttSubscribeStateCode.DIS_SUB
                }
            } catch (_: Exception) {
                throw DisSubMqttException()
            }
        }
    }

    /**
     * 배틀 매치 구독
     * @throws SubMqttException
     */
    override suspend fun subBattleMatch(roomId: Long) {

        if (mqttState.isConnect()) {
            try {
                if (mqttState.battleMatch == MqttState.MqttSubscribeStateCode.DIS_SUB) {
                    mqttState.battleMatchTopic =
                        "${context.getString(R.string.mqtt_battle_base_topic)}/match/$roomId"
                    mqttApi.subscribe(mqttState.battleMatchTopic)
                    mqttState.battleMatch = MqttState.MqttSubscribeStateCode.SUB
                }
            } catch (_: Exception) {
                throw SubMqttException()
            }
        }
    }

    /**
     * 배틀 매치 구독 해제
     * @throws DisSubMqttException
     */
    override suspend fun disSubBattleMatch() {

        if (mqttState.isConnect()) {
            try {
                if (mqttState.battleMatch == MqttState.MqttSubscribeStateCode.SUB) {
                    mqttApi.disSubscribe(mqttState.battleMatchTopic)
                    mqttState.battleMatchTopic = ""
                    mqttState.battleMatch = MqttState.MqttSubscribeStateCode.DIS_SUB
                }
            } catch (_: Exception) {
                throw DisSubMqttException()
            }
        }
    }

    /**
     * 배틀 입장 메시지 전송
     * @throws PubMqttException
     */
    override suspend fun pubBattleMatchEnter(roomId: Long, playerId: String) {

        if (mqttState.isConnect()) {
            try {

                val topic = "${context.getString(R.string.mqtt_battle_base_topic)}/enter/$roomId"

                mqttApi.produce(
                    topic = topic,
                    requestDto = EnterBattleRequestDto(playerId = playerId)
                )

            } catch (_: Exception) {
                throw PubMqttException()
            }
        }
    }

    /**
     * 배틀 선택 메시지 전송
     * @throws PubMqttException
     */
    override suspend fun pubBattleMatchPick(roomId: Long, playerId: String, targetPlayerId: String, pickCode: MatchRoundCode) {

        if (mqttState.isConnect()) {
            try {

                val topic = "${context.getString(R.string.mqtt_battle_base_topic)}/pick/$roomId"

                mqttApi.produce(
                    topic = topic,
                    requestDto = PickBattleRequestDto(
                        playerId = playerId,
                        targetPlayerId = targetPlayerId,
                        pickCode = pickCode
                    )
                )

            } catch (_: Exception) {
                throw PubMqttException()
            }
        }
    }

    /**
     * 배틀 퇴장 메시지 전송
     * @throws PubMqttException
     */
    override suspend fun pubBattleMatchExit(roomId: Long, playerId: String) {

        if (mqttState.isConnect()) {
            try {

                val topic = "${context.getString(R.string.mqtt_battle_base_topic)}/exit/$roomId"

                mqttApi.produce(
                    topic = topic,
                    requestDto = ExitBattleRequestDto(playerId = playerId)
                )

            } catch (_: Exception) {
                throw PubMqttException()
            }
        }
    }
}