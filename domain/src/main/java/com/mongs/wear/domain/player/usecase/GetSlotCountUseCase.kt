package com.mongs.wear.domain.player.usecase

import com.mongs.wear.core.exception.data.GetPlayerException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.GetSlotCountUseCaseException
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import com.mongs.wear.domain.player.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSlotCountUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
) : BaseNoParamUseCase<Int>() {

    /**
     * 슬롯 수 조회 UseCase
     * @throws GetPlayerException
     */
    override suspend fun execute(): Int {
        return withContext(Dispatchers.IO) {
            playerRepository.getSlotCount()
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is GetPlayerException -> throw GetSlotCountUseCaseException()

            else -> throw GetSlotCountUseCaseException()
        }
    }
}