package com.mongs.wear.domain.device.usecase

import com.mongs.wear.core.enums.MatchStateCode
import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.auth.repository.AuthRepository
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.device.exception.ConnectMqttException
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.domain.global.usecase.BaseNoParamUseCase
import com.mongs.wear.domain.management.repository.SlotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConnectMqttUseCase @Inject constructor(
    private val mqttClient: MqttClient,
    private val authRepository: AuthRepository,
    private val deviceRepository: DeviceRepository,
    private val slotRepository: SlotRepository,
    private val battleRepository: BattleRepository,
) : BaseNoParamUseCase<Unit>() {

    override suspend fun execute() {

        return withContext(Dispatchers.IO) {

            if (authRepository.isLogin()) {

                mqttClient.connect()

                if (mqttClient.isConnected()) {

                    // 현재 플레이어 정보 구독
                    deviceRepository.getAccountId().let { accountId ->
                        mqttClient.subPlayer(accountId = accountId)
                    }

                    // 현재 몽 정보 구독
                    slotRepository.getCurrentSlot()?.let { mongModel ->
                        mqttClient.subManager(mongId = mongModel.mongId)
                    }

                    // 배틀 정보 구독
                    val deviceId = deviceRepository.getDeviceId()

                    battleRepository.getMatch(deviceId = deviceId)?.let { matchModel ->
                        if (matchModel.stateCode == MatchStateCode.NONE) {
                            mqttClient.subSearchMatch(deviceId = deviceId)
                        } else {
                            mqttClient.subBattleMatch(roomId = matchModel.roomId)
                        }
                    }

                    // 네트워크 플래그 변경
                    deviceRepository.setNetwork(network = true)
                }
            }
        }
    }

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw ConnectMqttException()
        }
    }
}