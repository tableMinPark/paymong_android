package com.mongs.wear.data.manager.dto.response

import com.mongs.wear.core.enums.MongStateCode
import java.time.LocalDateTime

data class MongStateDto(

    val mongId: Long,

    val stateCode: MongStateCode,

    val isSleep: Boolean,

    val updatedAt: LocalDateTime,
)