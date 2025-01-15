package com.mongs.wear.data.device.dto.request

import java.time.LocalDateTime

data class ExchangeWalkingCountRequestDto(

    val mongId: Long,

    val totalWalkingCount: Int,

    val walkingCount: Int,

    val deviceBootedDt: LocalDateTime,
)
