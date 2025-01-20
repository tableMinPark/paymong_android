package com.mongs.wear.domain.battle.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.battle.exception.GetMatchException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.battle.vo.MatchVo
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.usecase.BaseNoParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMatchUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val battleRepository: BattleRepository,
) : BaseNoParamUseCase<LiveData<MatchVo>>() {

    override suspend fun execute(): LiveData<MatchVo> {

        return withContext(Dispatchers.IO) {
            battleRepository.getMatchLive(deviceId = deviceRepository.getDeviceId()).map { matchModel ->
                MatchVo(
                    roomId = matchModel.roomId,
                    round = matchModel.round,
                    isLastRound = matchModel.isLastRound,
                    stateCode = matchModel.stateCode,
                )
            }
        }
    }

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw GetMatchException()
        }
    }
}