package com.mongs.wear.data.manager.dto.response

import com.mongs.wear.core.enums.MongStatusCode
import java.time.LocalDateTime

data class MongStatusDto(

    val mongId: Long,

    val statusCode: MongStatusCode,

    val expRatio: Double,

    val weight: Double,

    val strengthRatio: Double,

    val satietyRatio: Double,

    val healthyRatio: Double,

    val fatigueRatio: Double,

    val poopCount: Int,

    val updatedAt: LocalDateTime,
)