package com.mongs.wear.data.manager.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mongs.wear.core.enums.MongStateCode
import com.mongs.wear.core.enums.MongStatusCode
import com.mongs.wear.data.manager.dto.response.MongBasicDto
import com.mongs.wear.data.manager.dto.response.MongStateDto
import com.mongs.wear.data.manager.dto.response.MongStatusDto
import com.mongs.wear.domain.management.model.MongModel
import java.time.LocalDateTime

@Entity(tableName = "mongs_mong")
data class MongEntity(

    @PrimaryKey
    val mongId: Long = 0L,

    var mongName: String,

    var payPoint: Int,

    var mongTypeCode: String,

    val createdAt: LocalDateTime,

    var weight: Double,

    var expRatio: Double,

    var healthyRatio: Double,

    var satietyRatio: Double,

    var strengthRatio: Double,

    var fatigueRatio: Double,

    var poopCount: Int,

    var stateCode: MongStateCode,

    var statusCode: MongStatusCode,

    var isSleeping: Boolean,

    /**
     * 로컬 필드
     */
    // 현재 몽 여부
    var isCurrent: Boolean = false,
    // 졸업 상태 확인 여부
    var graduateCheck: Boolean = false,

    /**
     * 무결성 필드
     */
    // 몽 기본 정보 수정 시각 (MongEntity)
    var basicUpdatedAt: LocalDateTime,
    // 몽 상태 수정 시각 (MongStateEntity)
    var stateUpdatedAt: LocalDateTime,
    // 몽 지수 수정 시각 (MongStatusEntity)
    var statusUpdatedAt: LocalDateTime,
) {
    companion object {
        fun of(
            mongBasicDto: MongBasicDto,
            mongStateDto: MongStateDto,
            mongStatusDto: MongStatusDto,
        ) : MongEntity = MongEntity(
            mongId = mongBasicDto.mongId,
            mongName = mongBasicDto.mongName,
            mongTypeCode = mongBasicDto.mongTypeCode,
            payPoint = mongBasicDto.payPoint,
            createdAt = mongBasicDto.createdAt,
            basicUpdatedAt = mongBasicDto.updatedAt,

            stateCode = mongStateDto.stateCode,
            isSleeping = mongStateDto.isSleep,
            stateUpdatedAt = mongStateDto.updatedAt,

            statusCode = mongStatusDto.statusCode,
            expRatio = mongStatusDto.expRatio,
            weight = mongStatusDto.weight,
            healthyRatio = mongStatusDto.healthyRatio,
            satietyRatio = mongStatusDto.satietyRatio,
            strengthRatio = mongStatusDto.strengthRatio,
            fatigueRatio = mongStatusDto.fatigueRatio,
            poopCount = mongStatusDto.poopCount,
            statusUpdatedAt = mongStatusDto.updatedAt
        )
    }

    fun update(
        mongBasicDto: MongBasicDto?,
        mongStateDto: MongStateDto?,
        mongStatusDto: MongStatusDto?,
    ) : MongEntity {

        mongBasicDto?.let { this.updateBasic(it) }
        mongStateDto?.let { this.updateState(it) }
        mongStatusDto?.let { this.updateStatus(it) }

        return this
    }

    /**
     * 몽 기본 정보 갱신
     */
    fun updateBasic(
        mongBasicDto: MongBasicDto,
    ) : MongEntity = this.updateBasic(
        mongName = mongBasicDto.mongName,
        mongTypeCode = mongBasicDto.mongTypeCode,
        payPoint = mongBasicDto.payPoint,
        updatedAt = mongBasicDto.updatedAt,
    )

    fun updateBasic(
        mongName: String = this.mongName,
        mongTypeCode: String = this.mongTypeCode,
        payPoint: Int = this.payPoint,
        updatedAt: LocalDateTime = this.basicUpdatedAt,
    ) : MongEntity {

        this.mongName = mongName
        this.mongTypeCode = mongTypeCode
        this.payPoint = payPoint
        this.basicUpdatedAt = updatedAt

        return this
    }

    /**
     * 몽 상태 갱신
     */
    fun updateState(
        mongStateDto: MongStateDto,
    ) : MongEntity = this.updateState(
        stateCode = mongStateDto.stateCode,
        isSleeping = mongStateDto.isSleep,
        updatedAt = mongStateDto.updatedAt,
    )

    fun updateState(
        stateCode: MongStateCode = this.stateCode,
        isSleeping: Boolean = this.isSleeping,
        updatedAt: LocalDateTime = this.stateUpdatedAt,
    ) : MongEntity {

        this.stateCode = stateCode
        this.isSleeping = isSleeping
        this.stateUpdatedAt = updatedAt

        return this
    }

    /**
     * 몽 지수 갱신
     */
    fun updateStatus(
        mongStatusDto: MongStatusDto,
    ) : MongEntity = this.updateStatus(
        statusCode = mongStatusDto.statusCode,
        weight = mongStatusDto.weight,
        expRatio = mongStatusDto.expRatio,
        healthyRatio = mongStatusDto.healthyRatio,
        satietyRatio = mongStatusDto.satietyRatio,
        strengthRatio = mongStatusDto.strengthRatio,
        fatigueRatio = mongStatusDto.fatigueRatio,
        poopCount = mongStatusDto.poopCount,
        updatedAt = mongStatusDto.updatedAt,
    )

    fun updateStatus(
        statusCode: MongStatusCode = this.statusCode,
        weight: Double = this.weight,
        expRatio: Double = this.expRatio,
        healthyRatio: Double = this.healthyRatio,
        satietyRatio: Double = this.satietyRatio,
        strengthRatio: Double = this.strengthRatio,
        fatigueRatio: Double = this.fatigueRatio,
        poopCount: Int = this.poopCount,
        updatedAt: LocalDateTime = this.statusUpdatedAt
    ): MongEntity {

        this.statusCode = statusCode
        this.weight = weight
        this.expRatio = expRatio
        this.healthyRatio = healthyRatio
        this.satietyRatio = satietyRatio
        this.strengthRatio = strengthRatio
        this.fatigueRatio = fatigueRatio
        this.poopCount = poopCount
        this.statusUpdatedAt = updatedAt

        return this
    }

    /**
     * 현재 몽 선택 헤제
     */
    fun unPick() : MongEntity {
        this.isCurrent = false
        return this
    }

    /**
     * 현재 몽 선택
     */
    fun pick() : MongEntity {
        this.isCurrent = true
        return this
    }

    /**
     * 졸업 상태 확인 여부 체크
     */
    fun graduateCheck() {
        this.graduateCheck = true
    }

    /**
     * UseCase Model 로 변환
     */
    fun toMongModel() = MongModel(
        mongId = this.mongId,
        mongName = this.mongName,
        payPoint = this.payPoint,
        mongTypeCode = this.mongTypeCode,
        createdAt = this.createdAt,
        weight = this.weight,
        expRatio = this.expRatio,
        healthyRatio = this.healthyRatio,
        satietyRatio = this.satietyRatio,
        strengthRatio = this.strengthRatio,
        fatigueRatio = this.fatigueRatio,
        poopCount = this.poopCount,
        stateCode = this.stateCode,
        statusCode = this.statusCode,
        isSleeping = this.isSleeping,
        isCurrent = this.isCurrent,
        graduateCheck = this.graduateCheck,
    )
}