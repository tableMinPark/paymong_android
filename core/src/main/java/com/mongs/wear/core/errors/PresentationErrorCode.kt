package com.mongs.wear.core.errors

enum class PresentationErrorCode(
    private val message: String,
    private val isMessageShow: Boolean,
) : ErrorCode {

    // Billing
    PRESENTATION_USER_BILLING_CONNECT("인앱 결제 시스템 연결 실패", false),
    PRESENTATION_USER_BILLING_NOT_SUPPORT("결제 지원하지 않는 기기", true),
    PRESENTATION_USER_GET_PRODUCTS("인앱 상품 조회 실패", false),

    // Auth
    PRESENTATION_AUTH_NOT_EXISTS_EMAIL("구글 이메일 없음", false),
    PRESENTATION_AUTH_NOT_EXISTS_NAME("구글 계정명 없음", false),
    PRESENTATION_AUTH_NOT_EXISTS_GOOGLE_ACCOUNT_ID("계정 정보 없음", false),
    ;

    override fun getMessage(): String {
        return this.message
    }

    override fun isMessageShow(): Boolean {
        return this.isMessageShow
    }
}