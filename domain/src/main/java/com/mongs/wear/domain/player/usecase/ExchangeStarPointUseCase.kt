package com.mongs.wear.domain.player.usecase

import com.mongs.wear.core.exception.data.ExchangeStarPointException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.ExchangeStarPointUseCaseException
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.domain.player.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExchangeStarPointUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
) : BaseParamUseCase<ExchangeStarPointUseCase.Param, Unit>() {

    /**
     * 스타 포인트 환전 UseCase
     * @throws ExchangeStarPointException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            playerRepository.exchangeStarPoint(
                mongId = param.mongId,
                starPoint = param.starPoint,
            )
        }
    }

    data class Param(

        val mongId: Long,

        val starPoint: Int,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is ExchangeStarPointException -> throw ExchangeStarPointUseCaseException()

            else -> throw ExchangeStarPointUseCaseException()
        }
    }
}