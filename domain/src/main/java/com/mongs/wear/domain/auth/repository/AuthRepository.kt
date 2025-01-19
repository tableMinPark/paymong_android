package com.mongs.wear.domain.auth.repository

interface AuthRepository {

    /**
     * 로그인 여부 확인
     */
    suspend fun isLogin() : Boolean

    /**
     * 회원 가입
     */
    suspend fun join(email: String, name: String, googleAccountId: String)

    /**
     * 로그인
     * @return accountId
     */
    suspend fun login(deviceId: String, email: String, googleAccountId: String) : Long

    /**
     * 로그 아웃
     */
    suspend fun logout()

    /**
     * 기기 등록
     */
    suspend fun createDevice(deviceId: String, fcmToken: String)
}