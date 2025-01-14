package com.mongs.wear.domain.battle.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.battle.exception.MatchWaitException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import com.mongs.wear.domain.management.repository.SlotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchWaitUseCase @Inject constructor(
    private val mqttClient: MqttClient,
    private val slotRepository: SlotRepository,
    private val deviceRepository: DeviceRepository,
    private val battleRepository: BattleRepository,
) : BaseParamUseCase<MatchWaitUseCase.Param, Unit>() {


    override suspend fun execute(param: Param) {

        withContext(Dispatchers.IO) {

//        val slowModel = slotRepository.getNowSlot()
//        val deviceId = appRepository.getDeviceId()
//
//        mqttClient.subSearchMatch(deviceId = deviceId)
//        battleRepository.matchWait(mongId = slowModel.mongId)
        }
    }

    data class Param(

        val matchFindCallback: suspend () -> Unit,

        val matchEnterCallback: suspend () -> Unit,
    )

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw MatchWaitException()
        }
    }
}