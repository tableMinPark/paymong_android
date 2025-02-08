package com.mongs.wear.domain.battle.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mongs.wear.core.exception.data.NotExistsMatchPlayerException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.GetRiverMatchPlayerUseCaseException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.battle.vo.MatchPlayerVo
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRiverMatchPlayerUseCase @Inject constructor(
    private val battleRepository: BattleRepository,
) : BaseNoParamUseCase<LiveData<MatchPlayerVo>>() {

    /**
     * 배틀 플레이어 정보 조회 (상대방) UseCase
     * @throws NotExistsMatchPlayerException
     */
    override suspend fun execute(): LiveData<MatchPlayerVo> {
        return withContext(Dispatchers.IO) {
            battleRepository.getRiverMatchPlayerLive().map { matchPlayerModel ->
                MatchPlayerVo(
                    playerId = matchPlayerModel.playerId,
                    mongTypeCode = matchPlayerModel.mongTypeCode,
                    hp = matchPlayerModel.hp,
                    roundCode = matchPlayerModel.roundCode,
                    isWinner = matchPlayerModel.isWin,
                )
            }
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is NotExistsMatchPlayerException -> throw GetRiverMatchPlayerUseCaseException()

            else -> throw GetRiverMatchPlayerUseCaseException()
        }
    }
}