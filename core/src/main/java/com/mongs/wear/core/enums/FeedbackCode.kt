package com.mongs.wear.core.enums

enum class FeedbackCode(
    val message: String,
    val secondaryMessage: String,
) {
    AUTH("인증", "로그인,로그아웃"),
    BATTLE("배틀", "매칭,배틀,승리보상"),
    COLLECTION("컬렉션", "컬렉션등록"),
    CONFIGURE("앱설정", "배경설정"),
    MANAGEMENT("캐릭터", "상호작용"),
    MEMBER("회원정보", "스타포인트"),
    SLOT("슬롯", "슬롯추가,슬롯삭제"),
    COMMON("기타", "이외"),
    ;
}