package com.paymong.wear.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.paymong.wear.data.code.Shift
import com.paymong.wear.data.code.State
import com.paymong.wear.domain.code.ShiftCode
import com.paymong.wear.domain.code.StateCode
import java.time.LocalDateTime

@Entity(tableName = "slot")
data class Slot(
    @PrimaryKey
    var mongId: Long = 0,
    var name: String = "",
    var mongCode: String = "",
    var weight: Double = 0.0,
    var healthy: Double = 0.0,
    var satiety: Double = 0.0,
    var strength: Double = 0.0,
    var sleep: Double = 0.0,
    var poopCount: Int = 0,
    var isSleeping: Boolean = false,
    var exp: Double = 0.0,
    var stateCode: StateCode = StateCode.EMPTY,
    var shiftCode: ShiftCode = ShiftCode.EMPTY,
    var payPoint: Int = 0,
    var born: LocalDateTime = LocalDateTime.now(),

    var isHappy: Boolean = false,
    var isEating: Boolean = false,
    var isSelected: Boolean = false,
    var isGraduateCheck: Boolean = false,
) {
    constructor(
        mongId: Long,
        name: String,
        mongCode: String,
        weight: Double,
        healthy: Double,
        satiety: Double,
        strength: Double,
        sleep: Double,
        poopCount: Int,
        isSleeping: Boolean,
        exp: Double,
        stateCode: String,
        shiftCode: String,
        payPoint: Int,
        born: LocalDateTime
    ) : this() {
        this.mongId = mongId
        this.name = name
        this.mongCode = mongCode
        this.weight = weight
        this.healthy = healthy
        this.satiety = satiety
        this.strength = strength
        this.sleep = sleep
        this.poopCount = poopCount
        this.isSleeping = isSleeping
        this.exp = exp
        this.stateCode = State.valueOf(stateCode).code
        this.shiftCode = Shift.valueOf(shiftCode).code
        this.payPoint = payPoint
        this.born = born
    }
}
