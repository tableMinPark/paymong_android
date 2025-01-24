package com.mongs.wear.domain.battle.usecase

import com.mongs.wear.core.enums.MatchRoundCode
import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.auth.exception.NotExistsEmailException
import com.mongs.wear.domain.auth.exception.NotExistsGoogleAccountIdException
import com.mongs.wear.domain.battle.exception.NotExistsPlayerIdException
import com.mongs.wear.domain.battle.exception.NotExistsTargetPlayerIdException
import com.mongs.wear.domain.battle.exception.PickMatchException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PickMatchUseCase @Inject constructor(
    private val battleRepository: BattleRepository,
) : BaseParamUseCase<PickMatchUseCase.Param, Unit>() {

    override suspend fun execute(param: Param) {

        withContext(Dispatchers.IO) {

            if (param.playerId.isNullOrEmpty()) throw NotExistsPlayerIdException()

            if (param.targetPlayerId.isNullOrEmpty()) throw NotExistsTargetPlayerIdException()

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

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw PickMatchException()
        }
    }
}