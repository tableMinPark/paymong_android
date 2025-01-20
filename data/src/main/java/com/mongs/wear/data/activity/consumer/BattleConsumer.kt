package com.mongs.wear.data.activity.consumer

import com.google.gson.Gson
import com.mongs.wear.data.activity.dto.response.CreateBattleResponseDto
import com.mongs.wear.data.activity.dto.response.FightBattleResponseDto
import com.mongs.wear.data.activity.dto.response.OverBattleResponseDto
import com.mongs.wear.data.activity.resolver.BattleObserveResolver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BattleConsumer @Inject constructor(
    private val observeResolver: BattleObserveResolver,
    private val gson: Gson,
) {

    companion object {
        private const val TAG = "BattleConsumer"

        private const val ACTIVITY_BATTLE_FIND_MATCHING = "ACTIVITY-BATTLE-200"
        private const val ACTIVITY_BATTLE_ENTER_ALL_BATTLE_PLAYER = "ACTIVITY-BATTLE-201"
        private const val ACTIVITY_BATTLE_OVER_BATTLE = "ACTIVITY-BATTLE-202"
        private const val ACTIVITY_BATTLE_FIGHT_BATTLE = "ACTIVITY-BATTLE-203"
    }

    fun messageArrived(code: String, resultJson: String) {
        when (code) {
            ACTIVITY_BATTLE_FIND_MATCHING ->
                observeResolver.updateSearchMatch(
                    createBattleResponseDto = gson.fromJson(
                        resultJson,
                        CreateBattleResponseDto::class.java
                    )
                )

            ACTIVITY_BATTLE_ENTER_ALL_BATTLE_PLAYER ->
                observeResolver.updateBattleMatchEnter(
                    fightBattleResponseDto = gson.fromJson(
                        resultJson,
                        FightBattleResponseDto::class.java
                    )
                )

            ACTIVITY_BATTLE_OVER_BATTLE ->
                observeResolver.updateBattleMatchOver(
                    overBattleResponseDto = gson.fromJson(
                        resultJson,
                        OverBattleResponseDto::class.java
                    )
                )

            ACTIVITY_BATTLE_FIGHT_BATTLE ->
                observeResolver.updateBattleMatchFight(
                    fightBattleResponseDto = gson.fromJson(
                        resultJson,
                        FightBattleResponseDto::class.java
                    )
                )

            else -> {}
        }
    }
}