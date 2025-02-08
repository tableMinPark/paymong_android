package com.mongs.wear.domain.auth.repository

interface AuthRepository {

    /**
     * 로그인 여부 조회
     */
    suspend fun isLogin() : Boolean

    /**
     * 회원 가입
     * @throws JoinException 회원 가입 실패
     */
    suspend fun join(email: String, name: String, googleAccountId: String)

    /**
     * 로그인
     * @throws NeedUpdateAppException 앱 업데이트 필요
     * @throws NeedJoinException 회원 가입 필요
     * @throws LoginException 로그인 실패
     */
    suspend fun login(deviceId: String, email: String, googleAccountId: String) : Long

    /**
     * 로그 아웃
     * @throws LogoutException 로그 아웃 실패
     */
    suspend fun logout()

    /**
     * 기기 등록
     * @throws CreateDeviceException 기기 등록 실패
     */
    suspend fun createDevice(deviceId: String, fcmToken: String)
}