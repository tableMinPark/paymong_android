package com.mongs.wear.domain.battle.usecase

import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.MatchStartUseCaseException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.core.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchStartUseCase @Inject constructor(
    private val battleRepository: BattleRepository,
) : BaseParamUseCase<MatchStartUseCase.Param, Unit>() {

    /**
     * 배틀 시작 UseCase
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            battleRepository.startMatch(roomId = param.roomId)
        }
    }

    data class Param(
        val roomId: Long,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            else -> throw MatchStartUseCaseException()
        }
    }
}