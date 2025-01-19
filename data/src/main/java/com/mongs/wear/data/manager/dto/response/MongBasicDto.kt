package com.mongs.wear.data.manager.dto.response

import java.time.LocalDateTime

data class MongBasicDto(

    val mongId: Long,

    val mongName: String,

    val mongTypeCode: String,

    val payPoint: Int,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime,
)