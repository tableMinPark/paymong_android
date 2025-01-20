package com.mongs.wear.core.enums

enum class MatchStateCode(
    val message: String
) {

    /**
     * 배틀 상태
     */
    NONE("없음"),
    MATCH_SEARCH("배틀 검색중"),
    MATCH_MATCHING("배틀 매칭 성공"),
    MATCH_ENTER("배틀 입장 완료"),
    MATCH_WAIT("배틀 라운드 선택 전 대기중"),
    MATCH_PICK_WAIT("배틀 라운드 선택 후 대기중"),
    MATCH_FIGHT("배틀 라운드 종료"),
    MATCH_OVER("배틀 종료"),
    MATCH_DELETE("매치 취소"),
}