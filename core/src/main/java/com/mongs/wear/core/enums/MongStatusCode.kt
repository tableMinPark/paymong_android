package com.mongs.wear.core.enums

enum class MongStatusCode(
    val message: String,
) {
    EMPTY("없음"),
    NORMAL("정상"),
    SOMNOLENCE("졸림"),
    HUNGRY("배고픔"),
    SICK("아픔"),
    ;
}