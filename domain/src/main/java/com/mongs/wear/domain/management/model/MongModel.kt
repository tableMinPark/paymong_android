package com.mongs.wear.domain.management.model

import com.mongs.wear.core.enums.MongStateCode
import com.mongs.wear.core.enums.MongStatusCode
import com.mongs.wear.domain.management.vo.MongVo
import java.time.LocalDateTime

data class MongModel(

    val mongId: Long,

    val mongName: String,

    val payPoint: Int,

    val mongTypeCode: String,

    val createdAt: LocalDateTime,

    val weight: Double,

    val expRatio: Double,

    val healthyRatio: Double,

    val satietyRatio: Double,

    val strengthRatio: Double,

    val fatigueRatio: Double,

    val poopCount: Int,

    val stateCode: MongStateCode,

    val statusCode: MongStatusCode,

    val isSleeping: Boolean,

    val isCurrent: Boolean,

    val graduateCheck: Boolean,
) {
    fun toMongVo() = MongVo(
        mongId = this.mongId,
        mongName = this.mongName,
        mongTypeCode = this.mongTypeCode,
        weight = this.weight,
        healthy = this.healthyRatio,
        satiety = this.satietyRatio,
        strength = this.strengthRatio,
        sleep = this.fatigueRatio,
        poopCount = this.poopCount,
        isSleeping = this.isSleeping,
        exp = this.expRatio,
        statusCode = this.statusCode,
        stateCode = this.stateCode,
        payPoint = this.payPoint,
        born = this.createdAt,
        graduateCheck = this.graduateCheck,
        isCurrent = this.isCurrent,
    )
}