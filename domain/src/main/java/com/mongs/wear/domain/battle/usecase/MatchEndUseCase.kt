package com.mongs.wear.domain.battle.usecase

import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.MatchEndUseCaseException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchEndUseCase @Inject constructor(
    private val mqttClient: MqttClient,
    private val battleRepository: BattleRepository,
) : BaseNoParamUseCase<Unit>() {

    /**
     * 매치 종료 UseCase
     */
    override suspend fun execute() {
        withContext(Dispatchers.IO) {
            // 매칭 결과 구독 해제
            mqttClient.disSubSearchMatch()

            // 배틀 매치 구독 해제
            mqttClient.disSubBattleMatch()

            // 배틀 매치 삭제
            battleRepository.deleteMatch()
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            else -> throw MatchEndUseCaseException()
        }
    }
}