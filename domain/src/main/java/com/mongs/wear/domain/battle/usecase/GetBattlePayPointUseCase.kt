package com.mongs.wear.domain.battle.usecase

import com.mongs.wear.core.exception.data.GetBattleRewardException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.GetBattlePayPointUseCaseException
import com.mongs.wear.domain.battle.repository.BattleRepository
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetBattlePayPointUseCase @Inject constructor(
    private val battleRepository: BattleRepository,
) : BaseNoParamUseCase<Int>() {

    /**
     * 배틀 보상 페이 포인트 조회 UseCase
     * @throws GetBattleRewardException
     */
    override suspend fun execute(): Int {
        return withContext(Dispatchers.IO) {
            battleRepository.getBattleReward().payPoint
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is GetBattleRewardException -> throw GetBattlePayPointUseCaseException()

            else -> throw GetBattlePayPointUseCaseException()
        }
    }
}