package com.mongs.wear.data.device.dto.request

import java.time.LocalDateTime

data class UpdateWalkingCountRequestDto(

    val totalWalkingCount: Int,

    val deviceBootedDt: LocalDateTime,
)
