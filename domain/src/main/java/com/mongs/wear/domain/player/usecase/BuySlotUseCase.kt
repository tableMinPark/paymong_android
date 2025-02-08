package com.mongs.wear.domain.player.usecase

import com.mongs.wear.core.exception.data.BuySlotException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import com.mongs.wear.core.exception.usecase.BuySlotUseCaseException
import com.mongs.wear.domain.player.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BuySlotUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
) : BaseNoParamUseCase<Unit>() {

    /**
     * 슬롯 구매 UseCase
     * @throws BuySlotException
     */
    override suspend fun execute() {
        withContext(Dispatchers.IO) {
            playerRepository.buySlot()
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is BuySlotException -> throw BuySlotUseCaseException()

            else -> throw BuySlotUseCaseException()
        }
    }
}