package com.mongs.wear.data.user.resolver

import com.mongs.wear.data.user.datastore.PlayerDataStore
import com.mongs.wear.data.user.dto.response.PlayerObserveResponseDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerObserveResolver @Inject constructor(
    private val playerDataStore: PlayerDataStore,
) {

    /**
     * 플레이어 정보 옵저빙
     */
    fun updatePlayer(playerObserveResponseDto: PlayerObserveResponseDto) = CoroutineScope(Dispatchers.IO).launch {
        playerDataStore.setStarPoint(starPoint = playerObserveResponseDto.starPoint)
    }
}