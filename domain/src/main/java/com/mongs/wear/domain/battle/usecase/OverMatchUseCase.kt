package com.mongs.wear.domain.battle.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.battle.exception.OverMatchException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OverMatchUseCase @Inject constructor(
    private val mqttClient: MqttClient,
    private val battleRepository: BattleRepository,
) : BaseParamUseCase<OverMatchUseCase.Param, Unit>() {

    override suspend fun execute(param: Param) {

        withContext(Dispatchers.IO) {

            mqttClient.disSubBattleMatch()

            battleRepository.updateOverMatch(roomId = param.roomId)
        }
    }

    data class Param(
        val roomId: Long,
    )

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw OverMatchException()
        }
    }
}