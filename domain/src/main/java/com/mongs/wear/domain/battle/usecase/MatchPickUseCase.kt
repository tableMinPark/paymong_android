package com.mongs.wear.domain.battle.usecase

import com.mongs.wear.core.enums.MatchRoundCode
import com.mongs.wear.core.exception.data.PickMatchException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.MatchPickUseCaseException
import com.mongs.wear.core.exception.usecase.NotExistsPlayerIdUseCaseException
import com.mongs.wear.core.exception.usecase.NotExistsTargetPlayerIdUseCaseException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.core.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchPickUseCase @Inject constructor(
    private val battleRepository: BattleRepository,
) : BaseParamUseCase<MatchPickUseCase.Param, Unit>() {

    /**
     * 배틀 선택 UseCase
     * @throws PickMatchException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {

            if (param.playerId.isNullOrEmpty()) throw NotExistsPlayerIdUseCaseException()

            if (param.targetPlayerId.isNullOrEmpty()) throw NotExistsTargetPlayerIdUseCaseException()

            when (param.pickCode) {
                MatchRoundCode.MATCH_PICK_ATTACK ->
                    battleRepository.pickMatch(roomId = param.roomId, targetPlayerId = param.targetPlayerId, pickCode = param.pickCode)

                MatchRoundCode.MATCH_PICK_DEFENCE ->
                    battleRepository.pickMatch(roomId = param.roomId, targetPlayerId = param.targetPlayerId, pickCode = param.pickCode)

                MatchRoundCode.MATCH_PICK_HEAL ->
                    battleRepository.pickMatch(roomId = param.roomId, targetPlayerId = param.targetPlayerId, pickCode = param.pickCode)

                else -> {}
            }
        }
    }

    data class Param(

        val roomId: Long,

        val playerId: String?,

        val targetPlayerId: String?,

        val pickCode: MatchRoundCode,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is PickMatchException -> throw MatchPickUseCaseException()

            else -> throw MatchPickUseCaseException()
        }
    }
}