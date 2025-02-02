package com.mongs.wear.domain.battle.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.domain.global.usecase.BaseNoParamUseCase
import com.mongs.wear.domain.training.exception.GetBattlePayPointException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetBattlePayPointUseCase @Inject constructor(
    private val battleRepository: BattleRepository,
) : BaseNoParamUseCase<Int>() {

    override suspend fun execute(): Int {

        return withContext(Dispatchers.IO) {
            battleRepository.getBattleReward().payPoint
        }
    }

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw GetBattlePayPointException()
        }
    }
}