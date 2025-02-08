package com.mongs.wear.domain.battle.usecase

import com.mongs.wear.core.exception.data.GetBattleException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.UpdateMatchUseCaseException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateMatchUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val battleRepository: BattleRepository,
) : BaseNoParamUseCase<Unit>() {

    /**
     * 배틀 정보 갱신 UseCase
     * @throws GetBattleException
     */
    override suspend fun execute() {
        withContext(Dispatchers.IO) {

            val deviceId = deviceRepository.getDeviceId();

            battleRepository.getMatch(deviceId = deviceId)?.let { matchModel ->
                battleRepository.updateMatch(roomId = matchModel.roomId)
            }
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is GetBattleException -> throw UpdateMatchUseCaseException()

            else -> throw UpdateMatchUseCaseException()
        }
    }
}