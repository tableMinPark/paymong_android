package com.paymong.wear.domain.viewModel.code

enum class SlotSelectCode(
    val message: String
) {
    STAND_BY("준비"),
    LOAD_MONG_LIST("몽 목록 로딩 중"),
    GENERATE_MONG("몽 생성 중"),
    SET_SLOT("슬롯 변경 중"),
    NAVIGATE("끝")
}