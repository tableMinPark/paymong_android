package com.mongs.wear.domain.battle.usecase

import com.mongs.wear.core.exception.data.EnterMatchException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.MatchEnterUseCaseException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.core.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchEnterUseCase @Inject constructor(
    private val mqttClient: MqttClient,
    private val battleRepository: BattleRepository,
) : BaseParamUseCase<MatchEnterUseCase.Param, Unit>() {

    /**
     * 배틀 입장 UseCase
     * @throws EnterMatchException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            // 매칭 결과 구독 해제
            mqttClient.disSubSearchMatch()

            // 배틀 매치 구독
            mqttClient.subBattleMatch(roomId = param.roomId)

            // 배틀 입장
            battleRepository.enterMatch(roomId = param.roomId)
        }
    }

    data class Param(
        val roomId: Long,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is EnterMatchException -> throw MatchEnterUseCaseException()

            else -> throw MatchEnterUseCaseException()
        }
    }
}