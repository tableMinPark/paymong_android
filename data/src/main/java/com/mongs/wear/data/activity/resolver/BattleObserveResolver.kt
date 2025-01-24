package com.mongs.wear.data.activity.resolver

import androidx.room.Transaction
import com.mongs.wear.core.enums.MatchStateCode
import com.mongs.wear.data.activity.dto.response.CreateBattleResponseDto
import com.mongs.wear.data.activity.dto.response.FightBattleResponseDto
import com.mongs.wear.data.activity.dto.response.OverBattleResponseDto
import com.mongs.wear.data.activity.entity.MatchPlayerEntity
import com.mongs.wear.data.device.datastore.DeviceDataStore
import com.mongs.wear.data.global.room.RoomDB
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BattleObserveResolver @Inject constructor(
    private val roomDB: RoomDB,
    private val deviceDataStore: DeviceDataStore,
) {

    /**
     * 매칭 성공 갱신
     */
    @Transaction
    fun updateSearchMatch(createBattleResponseDto: CreateBattleResponseDto) {

        val deviceId = deviceDataStore.getDeviceId()

        // 배틀 룸 정보 업데이트
        roomDB.matchRoomDao().let { dao ->
            dao.findByDeviceId(deviceId = deviceId)?.let { matchRoomEntity ->
                dao.save(
                    matchRoomEntity = matchRoomEntity.update(
                        roomId = createBattleResponseDto.roomId,
                        round = 0,
                        isLastRound = false,
                        stateCode = MatchStateCode.MATCH_MATCHING,
                    )
                )
            }
        }

        // 배틀 플레이어 저장
        roomDB.matchPlayerDao().let { dao ->
            createBattleResponseDto.battlePlayers.forEach({ battlePlayer ->
                dao.save(
                    matchPlayerEntity = MatchPlayerEntity(
                        playerId = battlePlayer.playerId,
                        deviceId = battlePlayer.deviceId,
                        roomId = createBattleResponseDto.roomId,
                        mongTypeCode = battlePlayer.mongTypeCode,
                        hp = battlePlayer.hp,
                        roundCode = battlePlayer.roundCode,
                        isMe = battlePlayer.deviceId == deviceId,
                        isWin = false
                    )
                )
            })
        }
    }

    /**
     * 모든 플레이어 입장 완료
     */
    @Transaction
    fun updateBattleMatchEnter(fightBattleResponseDto: FightBattleResponseDto) {

        // 배틀 룸 정보 업데이트
        roomDB.matchRoomDao().let { dao ->
            dao.findByRoomId(roomId = fightBattleResponseDto.roomId)?.let { matchRoomEntity ->
                dao.save(
                    matchRoomEntity = matchRoomEntity.update(
                        round = fightBattleResponseDto.round,
                        isLastRound = fightBattleResponseDto.isLastRound,
                        stateCode = MatchStateCode.MATCH_ENTER,
                    )
                )
            }
        }

        // 배틀 플레이어 정보 업데이트
        roomDB.matchPlayerDao().let { dao ->
            fightBattleResponseDto.battlePlayers.forEach({ battlePlayer ->
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

    /**
     * 매치 종료
     */
    @Transaction
    fun updateBattleMatchOver(overBattleResponseDto: OverBattleResponseDto) {

        // 배틀 룸 정보 업데이트
        roomDB.matchRoomDao().let { dao ->

            dao.findByRoomId(roomId = overBattleResponseDto.roomId)?.let { matchRoomEntity ->

                dao.save(
                    matchRoomEntity = matchRoomEntity.update(
                        isLastRound = true,
                        stateCode = MatchStateCode.MATCH_OVER,
                    )
                )
            }
        }

        roomDB.matchPlayerDao().let { dao ->
            dao.findByPlayerId(playerId = overBattleResponseDto.winPlayerId)
                ?.let { matchPlayerEntity ->
                    dao.save(matchPlayerEntity = matchPlayerEntity.update(isWin = true))
                }
        }
    }

    /**
     * 라운드 종료
     */
    @Transaction
    fun updateBattleMatchFight(fightBattleResponseDto: FightBattleResponseDto) {

        // 배틀 룸 정보 업데이트
        roomDB.matchRoomDao().let { dao ->
            dao.findByRoomId(roomId = fightBattleResponseDto.roomId)?.let { matchRoomEntity ->
                dao.save(
                    matchRoomEntity = matchRoomEntity.update(
                        round = fightBattleResponseDto.round,
                        isLastRound = fightBattleResponseDto.isLastRound,
                        stateCode = if(fightBattleResponseDto.isLastRound) MatchStateCode.MATCH_OVER else MatchStateCode.MATCH_FIGHT,
                    )
                )
            }
        }

        // 배틀 플레이어 정보 업데이트
        roomDB.matchPlayerDao().let { dao ->

            fightBattleResponseDto.battlePlayers.forEach({ battlePlayer ->

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
}