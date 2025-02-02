package com.mongs.wear.domain.battle.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.battle.exception.MatchExitException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchExitUseCase @Inject constructor(
    private val battleRepository: BattleRepository,
) : BaseParamUseCase<MatchExitUseCase.Param, Unit>() {

    override suspend fun execute(param: Param) {

        withContext(Dispatchers.IO) {
            // 배틀 퇴장
            battleRepository.exitMatch(roomId = param.roomId)
        }
    }

    data class Param(
        val roomId: Long
    )

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw MatchExitException()
        }
    }
}