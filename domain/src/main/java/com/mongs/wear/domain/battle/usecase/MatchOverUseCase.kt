package com.mongs.wear.domain.battle.usecase

import com.mongs.wear.core.exception.data.UpdateOverMatchException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.MatchOverUseCaseException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.core.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchOverUseCase @Inject constructor(
    private val battleRepository: BattleRepository,
) : BaseParamUseCase<MatchOverUseCase.Param, Unit>() {

    /**
     * 배틀 종료 UseCase
     * @throws UpdateOverMatchException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            battleRepository.updateOverMatch(roomId = param.roomId)
        }
    }

    data class Param(
        val roomId: Long,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is UpdateOverMatchException -> throw MatchOverUseCaseException()

            else -> throw MatchOverUseCaseException()
        }
    }
}