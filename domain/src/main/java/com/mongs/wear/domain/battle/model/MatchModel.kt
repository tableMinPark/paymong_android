package com.mongs.wear.domain.battle.model

import com.mongs.wear.core.enums.MatchStateCode

data class MatchModel(

    val roomId: Long,

    val round: Int,

    val isLastRound: Boolean,

    val stateCode: MatchStateCode,
)
