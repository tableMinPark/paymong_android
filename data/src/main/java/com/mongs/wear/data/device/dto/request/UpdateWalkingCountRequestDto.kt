package com.mongs.wear.data.device.dto.request

import java.time.LocalDateTime

data class UpdateWalkingCountRequestDto(

    val deviceId: String,

    val totalWalkingCount: Int,

    val deviceBootedDt: LocalDateTime,
)
