package com.mongs.wear.domain.battle.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mongs.wear.core.exception.data.CreateMatchException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.MatchWaitUseCaseException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.battle.vo.MatchVo
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import com.mongs.wear.domain.management.repository.SlotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchWaitUseCase @Inject constructor(
    private val mqttClient: MqttClient,
    private val slotRepository: SlotRepository,
    private val deviceRepository: DeviceRepository,
    private val battleRepository: BattleRepository,
) : BaseNoParamUseCase<LiveData<MatchVo?>>() {

    /**
     * 배틀 매칭 대기열 등록 UseCase
     * @throws CreateMatchException
     */
    override suspend fun execute(): LiveData<MatchVo?> {
        return withContext(Dispatchers.IO) {

            if (mqttClient.isConnected()) {
                slotRepository.getCurrentSlot()?.let { mongModel ->

                    val deviceId = deviceRepository.getDeviceId()

                    // 매칭 결과 구독 해제
                    mqttClient.disSubSearchMatch()

                    // 매치 구독 해제
                    mqttClient.disSubBattleMatch()

                    // 배틀룸 삭제
                    battleRepository.deleteMatch()

                    // 배틀룸 생성
                    val matchModel = battleRepository.createMatch(deviceId = deviceId)

                    // 매칭 결과 구독
                    mqttClient.subSearchMatch(deviceId = deviceId)

                    // 매칭 대기열 등록
                    battleRepository.createWaitMatching(mongId = mongModel.mongId)

                    matchModel.map { createdMatchModel ->
                        createdMatchModel?.let {
                            MatchVo(
                                roomId = createdMatchModel.roomId,
                                round = createdMatchModel.round,
                                isLastRound = createdMatchModel.isLastRound,
                                stateCode = createdMatchModel.stateCode,
                            )
                        } ?: run {
                            null
                        }
                    }
                } ?: run {
                    throw MatchWaitUseCaseException()
                }
            } else {
                throw MatchWaitUseCaseException()
            }
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is CreateMatchException -> throw MatchWaitUseCaseException()

            else -> throw MatchWaitUseCaseException()
        }
    }
}