package com.mongs.wear.core.enums

enum class MongStateCode(
    val message: String,
) {
    /**
     * 몽 상태 코드
     */
    NORMAL("정상 상태"),
    GRADUATE_READY("졸업 대기 상태"),
    EVOLUTION_READY("진화 대기 상태"),
    DEAD("사망"),
    GRADUATE("졸업"),
    DELETE("삭제"),
    ;
}