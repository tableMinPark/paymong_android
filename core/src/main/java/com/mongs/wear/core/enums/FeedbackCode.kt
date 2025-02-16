package com.mongs.wear.core.enums

enum class FeedbackCode(
    val message: String,
    val secondaryMessage: String,
    val needMessage: Boolean,
) {
    AUTH("인증", "로그인,로그아웃", false),
    BATTLE("배틀", "매칭,배틀,승리보상", false),
    TRAINING("훈련", "훈련오류,완료보상", false),
    COLLECTION("컬렉션", "컬렉션등록,컬렉션조회", false),
    CONFIGURE("앱설정", "걸음수누락,배경설정,권한설정", false),
    MANAGEMENT("캐릭터", "상호작용", false),
    MEMBER("회원정보", "스타포인트,환전", false),
    SLOT("슬롯", "슬롯추가,슬롯삭제,추가슬롯구매", false),
    STORE("구매", "스타포인트 충전,인앱상품구매", false),
    COMMON("기타", "이외", true),
    ;
}