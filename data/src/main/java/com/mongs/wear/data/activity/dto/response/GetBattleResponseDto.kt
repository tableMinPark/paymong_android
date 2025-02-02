package com.mongs.wear.data.activity.dto.response

import com.mongs.wear.data.activity.dto.etc.MatchPlayerDto

data class GetBattleResponseDto(

    val roomId: Long,

    val round: Int,

    val isLastRound: Boolean,

    val battlePlayers: Set<MatchPlayerDto>,
)
