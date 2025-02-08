package com.mongs.wear.domain.battle.usecase

import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.MatchExitUseCaseException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.core.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchExitUseCase @Inject constructor(
    private val battleRepository: BattleRepository,
) : BaseParamUseCase<MatchExitUseCase.Param, Unit>() {

    /**
     * 배틀 중도 퇴장 UseCase
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            // 배틀 퇴장
            battleRepository.exitMatch(roomId = param.roomId)
        }
    }

    data class Param(
        val roomId: Long
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            else -> throw MatchExitUseCaseException()
        }
    }
}