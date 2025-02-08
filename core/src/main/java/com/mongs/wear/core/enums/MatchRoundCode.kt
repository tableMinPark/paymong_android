package com.mongs.wear.core.enums

enum class MatchRoundCode(
    val message: String
) {
    /**
     * 선택
     */
    MATCH_PICK_ATTACK("배틀 공격 선택"),
    MATCH_PICK_DEFENCE("배틀 방어 선택"),
    MATCH_PICK_HEAL("배틀 회복 선택"),

    /**
     * 응답
     */
    NONE("원상태 유지"),
    MATCH_DEFENCE("배틀 방어"),
    MATCH_ATTACKED("배틀 피해"),
    MATCH_HEAL("배틀 회복"),
    MATCH_ATTACKED_HEAL("배틀 피해 & 회복"),
    ;
}