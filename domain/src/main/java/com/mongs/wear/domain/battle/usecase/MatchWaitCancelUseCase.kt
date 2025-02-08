package com.mongs.wear.domain.battle.usecase

import com.mongs.wear.core.exception.data.DeleteMatchException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.MatchWaitCancelUseCaseException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import com.mongs.wear.domain.management.repository.SlotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchWaitCancelUseCase @Inject constructor(
    private val mqttClient: MqttClient,
    private val slotRepository: SlotRepository,
    private val battleRepository: BattleRepository,
) : BaseNoParamUseCase<Unit>() {

    /**
     * 배틀 매칭 취소 UseCase
     * @throws DeleteMatchException
     */
    override suspend fun execute() {
        withContext(Dispatchers.IO) {
            slotRepository.getCurrentSlot()?.let { mongModel ->

                // 매칭 대기열 삭제
                battleRepository.deleteWaitMatching(mongId = mongModel.mongId)

                // 매칭 결과 구독 해제
                mqttClient.disSubSearchMatch()

                // 배틀 매치 구독 해제
                mqttClient.disSubBattleMatch()

                // 배틀 매치 삭제
                battleRepository.deleteMatch()
            }
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is DeleteMatchException -> throw MatchWaitCancelUseCaseException()

            else -> throw MatchWaitCancelUseCaseException()
        }
    }
}