package com.mongs.wear.domain.player.usecase

import androidx.lifecycle.LiveData
import com.mongs.wear.core.exception.data.GetPlayerException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.GetStarPointUseCaseException
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import com.mongs.wear.domain.player.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetStarPointUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
) : BaseNoParamUseCase<LiveData<Int>>() {

    /**
     * 스타 포인트 조회 UseCase
     * @throws GetPlayerException
     */
    override suspend fun execute(): LiveData<Int> {
        return withContext(Dispatchers.IO) {
            playerRepository.updatePlayer()
            playerRepository.getStarPointLive()
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is GetPlayerException -> throw GetStarPointUseCaseException()

            else -> throw GetStarPointUseCaseException()
        }
    }
}