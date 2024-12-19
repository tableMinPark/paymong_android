package com.mongs.wear.domain.battle.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.battle.vo.MatchPlayerVo
import javax.inject.Inject

class GetMyMatchPlayerUseCase @Inject constructor(
    private val battleRepository: BattleRepository,
) {
    suspend operator fun invoke(): LiveData<MatchPlayerVo> = battleRepository.getMyMatchPlayerLive().map { matchPlayerModel ->

        MatchPlayerVo(
            playerId = matchPlayerModel.playerId,
            mongTypeCode = matchPlayerModel.mongTypeCode,
            hp = matchPlayerModel.hp,
            roundCode = matchPlayerModel.roundCode,
            isWinner = matchPlayerModel.isWin,
        )
    }
}