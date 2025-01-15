package com.mongs.wear.data.device.dto.request

import java.time.LocalDateTime

data class CreateDeviceRequestDto(

    val deviceId: String,

    val totalWalkingCount: Int,

    val deviceBootedDt: LocalDateTime,

    val fcmToken: String,
)
