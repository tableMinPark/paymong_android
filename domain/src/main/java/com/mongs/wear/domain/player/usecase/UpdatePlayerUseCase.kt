package com.mongs.wear.domain.player.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.auth.repository.AuthRepository
import com.mongs.wear.domain.global.usecase.BaseNoParamUseCase
import com.mongs.wear.domain.player.exception.BuySlotException
import com.mongs.wear.domain.player.exception.UpdatePlayerException
import com.mongs.wear.domain.player.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdatePlayerUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val playerRepository: PlayerRepository,
) : BaseNoParamUseCase<Unit>() {

    override suspend fun execute() {

        withContext(Dispatchers.IO) {
            if (authRepository.isLogin()) {
                playerRepository.updatePlayer()
            }
        }
    }

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw UpdatePlayerException()
        }
    }
}