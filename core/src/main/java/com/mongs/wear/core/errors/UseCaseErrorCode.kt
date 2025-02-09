package com.mongs.wear.core.errors

enum class UseCaseErrorCode(
    private val message: String,
    private val isMessageShow: Boolean,
) : ErrorCode {

    // GLOBAL
    USE_CASE_GLOBAL_UNKNOWN_ERROR("알 수 없는 에러", false),
    USE_CASE_GLOBAL_DATA_ERROR("데이터 송수신 에러", false),
    USE_CASE_GLOBAL_CONNECT_MQTT_FAILED("네트워크 재연결 실패", false),
    USE_CASE_GLOBAL_PAUSE_CONNECT_MQTT_FAILED("네트워크 연결 일시중지 실패", false),
    USE_CASE_GLOBAL_DISCONNECT_MQTT_FAILED("네트워크 연결 해제 실패", false),

    // AUTH
    USE_CASE_AUTH_JOIN_FAILED("회원가입 실패", true),
    USE_CASE_AUTH_LOGIN_FAILED("로그인 실패", true),
    USE_CASE_AUTH_LOGOUT_FAILED("로그아웃 실패", true),
    USE_CASE_AUTH_NEED_JOIN_FAILED("회원가입 필요", false),
    USE_CASE_AUTH_NEED_UPDATE_APP_FAILED("앱 업데이트 필요", true),
    USE_CASE_AUTH_CREATE_DEVICE_FAILED("기기 등록 실패", false),

    // BATTLE
    USE_CASE_BATTLE_UPDATE_MATCH_FAILED("매치 정보 갱신 실패", false),
    USE_CASE_BATTLE_MATCH_EXIT_FAILED("매치 퇴장 실패", false),
    USE_CASE_BATTLE_GET_MATCH_FAILED("매치 조회 실패", false),
    USE_CASE_BATTLE_GET_MY_MATCH_PLAYER_FAILED("내 매치 조회 실패", false),
    USE_CASE_BATTLE_GET_RIVER_MATCH_PLAYER_FAILED("상대 매치 조회 실패", false),
    USE_CASE_BATTLE_MATCH_START_FAILED("매치 시작 실패", false),
    USE_CASE_BATTLE_MATCH_END_FAILED("매치 종료 실패", false),
    USE_CASE_BATTLE_MATCH_ENTER_FAILED("매치 시작 실패", false),
    USE_CASE_BATTLE_MATCH_WAIT_CANCEL_FAILED("매치 대기 취소 실패", false),
    USE_CASE_BATTLE_MATCH_WAIT_FAILED("잠시후 다시시도", true),
    USE_CASE_BATTLE_OVER_MATCH_FAILED("매치 종료 실패", false),
    USE_CASE_BATTLE_PICK_MATCH_FAILED("매치 선택 실패", false),
    USE_CASE_BATTLE_NOT_EXISTS_PLAYER_ID_FAILED("매치 선택 실패", false),
    USE_CASE_BATTLE_NOT_EXISTS_TARGET_PLAYER_ID_FAILED("매치 선택 실패", false),

    // TRAINING
    USE_CASE_GET_BATTLE_PAY_POINT_FAILED("잠시후 재시도", true),
    USE_CASE_GET_TRAINING_PAY_POINT_FAILED("잠시후 재시도", true),
    USE_CASE_TRAINING_MONG_FAILED("훈련 완료 실패", true),

    // COLLECTION
    USE_CASE_COLLECTION_CREATE_MAP_COLLECTIONS_FAILED("컬렉션 맵 등록 실패", false),
    USE_CASE_COLLECTION_GET_MAP_COLLECTIONS_FAILED("잠시후 다시시도", true),
    USE_CASE_COLLECTION_GET_MONG_COLLECTIONS_FAILED("잠시후 다시시도", true),

    // FEEDBACK
    USE_CASE_FEEDBACK_CREATE_FEEDBACK_FAILED("오류 신고 실패", true),

    // MANAGEMENT
    USE_CASE_MANAGEMENT_UPDATE_CURRENT_SLOT_FAILED("현재 몽 정보 갱신 실패", false),
    USE_CASE_MANAGEMENT_CREATE_MONG_FAILED("생성 실패", true),
    USE_CASE_MANAGEMENT_DELETE_MONG_FAILED("삭제 실패", true),
    USE_CASE_MANAGEMENT_EVOLUTION_MONG_FAILED("몽 진화 실패", true),
    USE_CASE_MANAGEMENT_FEED_MONG_FAILED("%d초 후 구매 가능", true),
    USE_CASE_MANAGEMENT_GET_CURRENT_SLOT_FAILED("슬롯 조회 실패", false),
    USE_CASE_MANAGEMENT_GET_FOOD_CODES_FAILED("밥 조회 실패", false),
    USE_CASE_MANAGEMENT_FOOD_CODES_EMPTY("구매 가능한 밥이 없음", true),
    USE_CASE_MANAGEMENT_GET_SLOTS_FAILED("몽 조회 실패", false),
    USE_CASE_MANAGEMENT_SNACK_CODES_FAILED("간식 조회 실패", false),
    USE_CASE_MANAGEMENT_SNACK_CODES_EMPTY("구매 가능한 간식이 없음", true),
    USE_CASE_MANAGEMENT_GRADUATE_CHECK_MONG_FAILED("몽 졸업 체크 실패", false),
    USE_CASE_MANAGEMENT_GRADUATE_MONG_FAILED("졸업 실패", true),
    USE_CASE_MANAGEMENT_POOP_CLEAN_MONG_FAILED("청소 실패", true),
    USE_CASE_MANAGEMENT_SET_CURRENT_SLOT_FAILED("슬롯 설정 실패", false),
    USE_CASE_MANAGEMENT_SLEEP_MONG_FAILED("수면 상태 변경 실패", true),
    USE_CASE_MANAGEMENT_STROKE_MONG_FAILED("%d초 뒤 가능", true),

    // PLAYER
    USE_CASE_PLAYER_UPDATE_PLAYER_FAILED("현재 플레이어 정보 갱신 실패", false),
    USE_CASE_PLAYER_EXCHANGE_STAR_POINT_FAILED("스타 포인트 환전 실패", true),
    USE_CASE_PLAYER_BUY_SLOT_FAILED("슬롯 구매 실패", true),
    USE_CASE_PLAYER_GET_SLOT_COUNT_FAILED("슬롯 수 조회 실패", false),
    USE_CASE_PLAYER_GET_STAR_POINT_FAILED("스타 포인트 조회 실패", false),

    // DEVICE
    USE_CASE_DEVICE_GET_BACKGROUND_MAP_CODE_FAILED("배경 조회 실패", false),
    USE_CASE_DEVICE_SET_BACKGROUND_MAP_CODE_FAILED("배경 변경 실패", true),
    USE_CASE_DEVICE_GET_NETWORK_FAILED("네트워크 FLAG 조회 실패", false),
    USE_CASE_DEVICE_SET_NETWORK_FAILED("네트워크 FLAG 변경 실패", false),
    USE_CASE_DEVICE_GET_NOTIFICATION_FAILED("알림 FLAG 조회 실패", false),
    USE_CASE_DEVICE_SET_NOTIFICATION_FAILED("알림 FLAG 변경 실패", false),
    USE_CASE_DEVICE_GET_SOUND_VOLUME_FAILED("음량 조회 실패", false),
    USE_CASE_DEVICE_SET_SOUND_VOLUME_FAILED("음량 변경 실패", false),
    USE_CASE_DEVICE_SET_DEVICE_ID_FAILED("기기 ID 변경 실패", false),
    USE_CASE_PLAYER_GET_STEPS_FAILED("걸음 수 조회 실패", false),
    USE_CASE_PLAYER_EXCHANGE_WALKING_COUNT_FAILED("걸음 수 환전 실패", true),
    USE_CASE_PLAYER_SET_LOCAL_TOTAL_WALKING_COUNT_FAILED("로컬 총 걸음 수 갱신 실패", false),
    USE_CASE_PLAYER_SET_SERVER_TOTAL_WALKING_COUNT_FAILED("서버 총 걸음 수 갱신 실패", false),

    // STORE
    USE_CASE_STORE_CONSUME_PRODUCT_ORDER_FAILED("상품 주문 소비 실패", true),
    USE_CASE_STORE_GET_CONSUMED_ORDER_IDS_FAILED("소비 주문 ID 조회 실패", false),
    USE_CASE_STORE_GET_PRODUCT_IDS_FAILED("상품 ID 조회 실패", false),
    ;

    override fun getMessage(): String {
        return this.message
    }

    override fun isMessageShow(): Boolean {
        return this.isMessageShow
    }
}