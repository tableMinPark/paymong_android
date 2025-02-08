package com.mongs.wear.data.activity.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mongs.wear.core.enums.MatchRoundCode

@Entity("mongs_match_player")
data class MatchPlayerEntity(

    @PrimaryKey
    val playerId: String,

    val deviceId: String,

    val roomId: Long,

    val mongTypeCode: String,

    val isMe: Boolean,

    var hp: Double,

    var roundCode: MatchRoundCode,

    var isWin: Boolean,

) {
    fun update(
        hp: Double = this.hp,
        roundCode: MatchRoundCode = this.roundCode,
        isWin: Boolean = this.isWin
    ) : MatchPlayerEntity {

        this.hp = hp
        this.roundCode = roundCode
        this.isWin = isWin

        return this
    }
}
