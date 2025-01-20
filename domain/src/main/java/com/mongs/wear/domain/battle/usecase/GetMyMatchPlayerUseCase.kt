package com.mongs.wear.domain.battle.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.battle.exception.GetMyMatchException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.battle.vo.MatchPlayerVo
import com.mongs.wear.domain.global.usecase.BaseNoParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMyMatchPlayerUseCase @Inject constructor(
    private val battleRepository: BattleRepository,
) : BaseNoParamUseCase<LiveData<MatchPlayerVo>>() {

    override suspend fun execute(): LiveData<MatchPlayerVo> {

        return withContext(Dispatchers.IO) {
            battleRepository.getMyMatchPlayerLive().map { matchPlayerModel ->
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

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw GetMyMatchException()
        }
    }
}