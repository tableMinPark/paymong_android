package com.mongs.wear.core.errors

enum class DataErrorCode(
    private val message: String,
    private val isMessageShow: Boolean,
) : ErrorCode {

    // GLOBAL
    DATA_GLOBAL_MQTT_CONNECT("MQTT 연결 실패", false),
    DATA_GLOBAL_MQTT_PUB("MQTT 전송 실패", false),
    DATA_GLOBAL_MQTT_SUB("MQTT 구독 실패", false),
    DATA_GLOBAL_MQTT_PAUSE("MQTT 구독 일시 중지 실패", false),
    DATA_GLOBAL_MQTT_DIS_SUB("MQTT 구독 해제 실패", false),
    DATA_GLOBAL_MQTT_DISCONNECT("MQTT 연결 해제 실패", false),

    // ACTIVITY-BATTLE
    DATA_ACTIVITY_BATTLE_GET_BATTLE("배틀 정보 조회 실패", false),
    DATA_ACTIVITY_BATTLE_GET_BATTLE_REWARD("배틀 보상 정보 조회 실패", false),
    DATA_ACTIVITY_BATTLE_UPDATE_OVER_MATCH("배틀 매치 결과 조회 실패", false),
    DATA_ACTIVITY_BATTLE_CREATE_MATCH("배틀 매칭 등록 실패", false),
    DATA_ACTIVITY_BATTLE_DELETE_MATCH("배틀 매칭 삭제 실패", false),
    DATA_ACTIVITY_BATTLE_ENTER_MATCH("배틀 매치 입장 실패", false),
    DATA_ACTIVITY_BATTLE_EXIT_MATCH("배틀 매치 퇴장 실패", false),
    DATA_ACTIVITY_BATTLE_NOT_EXISTS_MATCH("배틀 매칭이 존재하지 않음", false),
    DATA_ACTIVITY_BATTLE_NOT_EXISTS_MATCH_PLAYER("배틀 플레이어가 존재하지 않음", false),
    DATA_ACTIVITY_BATTLE_PICK_MATCH("배틀 매치 선택 실패", false),

    // ACTIVITY-TRAINING
    DATA_ACTIVITY_GET_TRAINING_RUNNER("훈련 러너 정보 조회 실패", false),
    DATA_ACTIVITY_TRAINING_RUNNER("훈련 러너 완료 실패", false),

    // AUTH
    DATA_AUTH_JOIN("회원가입에 실패했습니다.", false),
    DATA_AUTH_LOGIN("로그인에 실패했습니다.", false),
    DATA_AUTH_LOGOUT("로그인에 실패했습니다.", false),
    DATA_AUTH_NEED_JOIN("회원가입이 필요합니다.", false),
    DATA_AUTH_NEED_UPDATE_APP("앱 업데이트가 필요합니다.", false),
    DATA_AUTH_REISSUE("로그인 연장에 실패했습니다.", false),
    DATA_AUTH_CREATE_DEVICE("기기 정보 등록 실패했습니다.", false),

    // MANAGER
    DATA_MANAGER_MANAGEMENT_CREATE_MONG("몽 생성에 실패했습니다.", false),
    DATA_MANAGER_MANAGEMENT_DELETE_MONG("몽 삭제에 실패했습니다.", false),
    DATA_MANAGER_MANAGEMENT_EVOLUTION_MONG("몽 진화에 실패했습니다.", false),
    DATA_MANAGER_MANAGEMENT_FEED_MONG("몽 먹이 주기에 실패했습니다.", false),
    DATA_MANAGER_MANAGEMENT_GET_FEED_ITEMS("먹이 목록 조회에 실패했습니다.", false),
    DATA_MANAGER_MANAGEMENT_GRADUATE_MONG("몽 진화에 실패했습니다.", false),
    DATA_MANAGER_MANAGEMENT_POOP_CLEAN_MONG("몽 배변처리에 실패했습니다.", false),
    DATA_MANAGER_MANAGEMENT_SLEEP_MONG("몽 수면/기상에 실패했습니다.", false),
    DATA_MANAGER_MANAGEMENT_STROKE_MONG("쓰다듬을 수 없습니다.", false),

    // USER-DEVICE
    DATA_USER_DEVICE_EXCHANGE_WALKING("걸음 수 환전 실패했습니다.", false),
    // USER-COLLECTION
    DATA_USER_COLLECTION_CREATE_MAP_COLLECTIONS("컬렉션 맵 등록 실패했습니다.", false),
    DATA_USER_COLLECTION_GET_MAP_COLLECTIONS("컬렉션 맵 조회 실패했습니다.", false),
    DATA_USER_COLLECTION_GET_MONG_COLLECTIONS("컬렉션 맵 조회 실패했습니다.", false),
    // USER-FEEDBACK
    DATA_USER_FEEDBACK_CREATE_FEEDBACK("오류 신고 실패했습니다.", false),
    // USER-PLAYER
    DATA_USER_PLAYER_CREATE_PLAYER("플레이어 정보 등록 실패했습니다.", false),
    DATA_USER_PLAYER_GET_PLAYER("플레이어 정보 조회 실패했습니다.", false),
    DATA_USER_PLAYER_BUY_SLOT("슬롯 구매 실패했습니다.", false),
    DATA_USER_PLAYER_EXCHANGE_STAR_POINT("스타포인트 환전 실패했습니다.", false),
    // USER-STORE
    DATA_USER_STORE_GET_PRODUCT_IDS("상품 목록 조회에 실패했습니다.", false),
    DATA_USER_STORE_CONSUME_PRODUCT_ORDER("상품 소비에 실패했습니다.", false),
    DATA_USER_STORE_CONSUMED_ORDER_IDS("상품 소비 ID 목록 조회에 실패했습니다.", false),
    ;

    override fun getMessage(): String {
        return this.message
    }

    override fun isMessageShow(): Boolean {
        return this.isMessageShow
    }
}