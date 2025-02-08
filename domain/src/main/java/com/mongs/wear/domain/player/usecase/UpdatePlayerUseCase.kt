package com.mongs.wear.domain.player.usecase

import com.mongs.wear.core.exception.data.GetPlayerException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.domain.auth.repository.AuthRepository
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import com.mongs.wear.core.exception.usecase.UpdatePlayerUseCaseException
import com.mongs.wear.domain.player.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdatePlayerUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val playerRepository: PlayerRepository,
) : BaseNoParamUseCase<Unit>() {

    /**
     * 플레이어 정보 갱신 UseCase
     * @throws GetPlayerException
     */
    override suspend fun execute() {
        withContext(Dispatchers.IO) {
            if (authRepository.isLogin()) {
                playerRepository.updatePlayer()
            }
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is GetPlayerException -> throw UpdatePlayerUseCaseException()

            else -> throw UpdatePlayerUseCaseException()
        }
    }
}