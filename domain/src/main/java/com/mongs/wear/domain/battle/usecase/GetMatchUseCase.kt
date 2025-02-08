package com.mongs.wear.domain.battle.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mongs.wear.core.exception.data.NotExistsMatchException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.GetMatchUseCaseException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.battle.vo.MatchVo
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMatchUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val battleRepository: BattleRepository,
) : BaseNoParamUseCase<LiveData<MatchVo>>() {

    /**
     * 배틀 정보 조회 UseCase
     * @throws NotExistsMatchException
     */
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

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is NotExistsMatchException -> throw GetMatchUseCaseException()

            else -> throw GetMatchUseCaseException()
        }
    }
}