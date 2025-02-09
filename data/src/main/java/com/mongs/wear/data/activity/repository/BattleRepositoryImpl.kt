package com.mongs.wear.data.activity.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mongs.wear.core.enums.MatchRoundCode
import com.mongs.wear.core.enums.MatchStateCode
import com.mongs.wear.data.activity.api.BattleApi
import com.mongs.wear.data.activity.entity.MatchRoomEntity
import com.mongs.wear.core.exception.data.CreateMatchException
import com.mongs.wear.core.exception.data.DeleteMatchException
import com.mongs.wear.core.exception.data.EnterMatchException
import com.mongs.wear.core.exception.data.ExitMatchException
import com.mongs.wear.core.exception.data.GetBattleException
import com.mongs.wear.core.exception.data.GetBattleRewardException
import com.mongs.wear.core.exception.data.NotExistsMatchException
import com.mongs.wear.core.exception.data.NotExistsMatchPlayerException
import com.mongs.wear.core.exception.data.PickMatchException
import com.mongs.wear.core.exception.data.UpdateOverMatchException
import com.mongs.wear.core.exception.data.PubMqttException
import com.mongs.wear.data.global.room.RoomDB
import com.mongs.wear.data.global.utils.HttpUtil
import com.mongs.wear.domain.battle.model.BattleRewardModel
import com.mongs.wear.domain.battle.model.MatchModel
import com.mongs.wear.domain.battle.model.MatchPlayerModel
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.global.client.MqttClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BattleRepositoryImpl @Inject constructor(
    private val roomDB: RoomDB,
    private val battleApi: BattleApi,
    private val mqttClient: MqttClient,
): BattleRepository {

    /**
     * 배틀 정보 갱신
     * @throws GetBattleException
     */
    override suspend fun updateMatch(roomId: Long) {

        val response = battleApi.getBattle(roomId = roomId)

        if (response.isSuccessful) {
            response.body()?.let { body ->
                // 배틀 룸 정보 업데이트
                roomDB.matchRoomDao().let { dao ->
                    dao.findByRoomId(roomId = body.result.roomId)?.let { matchRoomEntity ->
                        dao.save(
                            matchRoomEntity = matchRoomEntity.update(
                                round = body.result.round,
                                isLastRound = body.result.isLastRound,
                                stateCode = MatchStateCode.MATCH_FIGHT,
                            )
                        )
                    }
                }

                // 배틀 플레이어 정보 업데이트
                roomDB.matchPlayerDao().let { dao ->
                    body.result.battlePlayers.forEach({ battlePlayer ->
                        dao.findByPlayerId(playerId = battlePlayer.playerId)?.let { matchPlayerEntity ->
                            dao.save(
                                matchPlayerEntity = matchPlayerEntity.update(
                                    hp = battlePlayer.hp,
                                    roundCode = battlePlayer.roundCode,
                                )
                            )
                        }
                    })
                }
            }
        } else {
            throw GetBattleException(result = HttpUtil.getErrorResult(response.errorBody()))
        }
    }

    /**
     * 배틀 보상 정보 조회
     * @throws GetBattleRewardException
     */
    override suspend fun getBattleReward(): BattleRewardModel {

        val response = battleApi.getBattleReward()

        if (response.isSuccessful) {
            response.body()?.let { body ->
                return BattleRewardModel(
                    rewardPayPoint = body.result.rewardPayPoint,
                    bettingPayPoint = body.result.bettingPayPoint,
                )
            }
        }

        throw GetBattleRewardException(result = HttpUtil.getErrorResult(response.errorBody()))
    }

    /**
     * 매칭 대기열 등록
     * @throws CreateMatchException
     */
    override suspend fun createWaitMatching(mongId: Long) {

        val response = battleApi.createWaitMatching(mongId = mongId)

        if (!response.isSuccessful) {
            throw CreateMatchException(result = HttpUtil.getErrorResult(response.errorBody()))
        }
    }

    /**
     * 매칭 대기열 삭제
     * @throws DeleteMatchException
     */
    override suspend fun deleteWaitMatching(mongId: Long) {

        val response = battleApi.deleteWaitMatching(mongId = mongId)

        if (!response.isSuccessful) {
            throw DeleteMatchException(result = HttpUtil.getErrorResult(response.errorBody()))
        }
    }

    /**
     * 배틀룸 로컬 등록
     */
    override suspend fun createMatch(deviceId: String): LiveData<MatchModel?> {
        return roomDB.matchRoomDao().let { dao ->
            // 새로운 배틀룸 등록
            dao.save(
                MatchRoomEntity(
                    deviceId = deviceId,
                    roomId = -1,
                    round = -1,
                    isLastRound = false,
                    stateCode = MatchStateCode.NONE,
                )
            )

            // 등록한 배틀룸 Live 객체 조회
            dao.findLiveByDeviceId(deviceId = deviceId).map { matchRoomEntity ->
                matchRoomEntity?.toMatchModel()
            }
        }
    }

    /**
     * 배틀룸 로컬 삭제
     */
    override suspend fun deleteMatch() {
        // 모든 플레이어 삭제
        roomDB.matchPlayerDao().deleteAll()
        // 모든 배틀룸 삭제
        roomDB.matchRoomDao().deleteAll()
    }

    /**
     * 매칭 입장
     * @throws EnterMatchException
     */
    override suspend fun enterMatch(roomId: Long) {

        roomDB.matchPlayerDao().findPlayerIdByIsMeTrue()?.let { playerId ->

            try {
                mqttClient.pubBattleMatchEnter(roomId = roomId, playerId = playerId)
            } catch (e: PubMqttException) {
                throw EnterMatchException()
            }
        }
    }

    /**
     * 매치 조회
     */
    override suspend fun getMatch(deviceId: String): MatchModel? {
        return roomDB.matchRoomDao().findByDeviceId(deviceId = deviceId)?.toMatchModel() ?: run { null }
    }

    /**
     * 배틀룸 Live 객체 조회
     * @throws NotExistsMatchException
     */
    override suspend fun getMatchLive(deviceId: String): LiveData<MatchModel> {

        return roomDB.matchRoomDao().findLiveByDeviceId(deviceId = deviceId).map { matchRoomEntity ->
            matchRoomEntity?.toMatchModel() ?: run {
                throw NotExistsMatchException()
            }
        }
    }

    /**
     * 나의 매치 플레이어 라이브 객체 조회
     * @throws NotExistsMatchPlayerException
     */
    override suspend fun getMyMatchPlayerLive(): LiveData<MatchPlayerModel> {

        return roomDB.matchPlayerDao().findLiveByPlayerIdIsMeTrue().map { matchPlayerEntity ->
            matchPlayerEntity?.let {
                MatchPlayerModel(
                    playerId = matchPlayerEntity.playerId,
                    mongTypeCode = matchPlayerEntity.mongTypeCode,
                    hp = matchPlayerEntity.hp,
                    roundCode = matchPlayerEntity.roundCode,
                    isMe = matchPlayerEntity.isMe,
                    isWin = matchPlayerEntity.isWin,
                )
            } ?: run {
                throw NotExistsMatchPlayerException()
            }
        }
    }

    /**
     * 상대방 매치 플레이어 라이브 객체 조회
     * @throws NotExistsMatchPlayerException
     */
    override suspend fun getRiverMatchPlayerLive(): LiveData<MatchPlayerModel> {

        return roomDB.matchPlayerDao().findLiveByPlayerIdIsMeFalse().map { matchPlayerEntity ->
            matchPlayerEntity?.let {
                MatchPlayerModel(
                    playerId = matchPlayerEntity.playerId,
                    mongTypeCode = matchPlayerEntity.mongTypeCode,
                    hp = matchPlayerEntity.hp,
                    roundCode = matchPlayerEntity.roundCode,
                    isMe = matchPlayerEntity.isMe,
                    isWin = matchPlayerEntity.isWin,
                )
            } ?: run {
                throw NotExistsMatchPlayerException()
            }
        }
    }

    /**
     * 매치 시작
     */
    override suspend fun startMatch(roomId: Long) {

        roomDB.matchRoomDao().let { dao ->
            dao.findByRoomId(roomId = roomId)?.let { matchRoomEntity ->
                dao.save(
                    matchRoomEntity.update(
                        stateCode = MatchStateCode.MATCH_WAIT
                    )
                )
            }
        }
    }

    /**
     * 매치 퇴장
     * @throws ExitMatchException
     */
    override suspend fun exitMatch(roomId: Long) {

        roomDB.matchPlayerDao().findPlayerIdByIsMeTrue()?.let { playerId ->
            try {
                mqttClient.pubBattleMatchExit(roomId = roomId, playerId = playerId)
            } catch (e: PubMqttException) {
                throw ExitMatchException()
            }
        }
    }

    /**
     * 매치 선택
     * @throws PickMatchException
     */
    override suspend fun pickMatch(roomId: Long, targetPlayerId: String, pickCode: MatchRoundCode) {

        roomDB.matchPlayerDao().findPlayerIdByIsMeTrue()?.let { playerId ->
            try {
                mqttClient.pubBattleMatchPick(
                    roomId = roomId,
                    playerId = playerId,
                    targetPlayerId = targetPlayerId,
                    pickCode = pickCode
                )
            } catch (e: PubMqttException) {
                throw PickMatchException()
            }
        }

        roomDB.matchRoomDao().let { dao ->
            dao.findByRoomId(roomId = roomId)?.let { matchRoomEntity ->
                dao.save(
                    matchRoomEntity.update(
                        stateCode = MatchStateCode.MATCH_PICK_WAIT
                    )
                )
            }
        }
    }

    /**
     * 매치 결과 정보 업데이트
     * @throws UpdateOverMatchException
     */
    override suspend fun updateOverMatch(roomId: Long) {

        val response = battleApi.getOverBattle(roomId = roomId)

        if (response.isSuccessful) {
            response.body()?.let { body ->
                roomDB.matchRoomDao().let { dao ->
                    dao.findByRoomId(roomId = roomId)?.let { matchRoomEntity ->
                        dao.save(
                            matchRoomEntity.update(
                                isLastRound = true,
                                stateCode = MatchStateCode.MATCH_OVER,
                            )
                        )
                    }
                }

                roomDB.matchPlayerDao().let { dao ->
                    dao.findByPlayerId(playerId = body.result.winPlayerId)?.let { matchPlayerEntity ->
                        dao.save(
                            matchPlayerEntity.update(
                                isWin = true,
                            )
                        )
                    }
                }
            }
        } else {
            throw UpdateOverMatchException(result = HttpUtil.getErrorResult(response.errorBody()))
        }
    }
}