package com.mongs.wear.data.auth.api

import com.mongs.wear.core.dto.response.ResponseDto
import com.mongs.wear.data.auth.dto.request.JoinRequestDto
import com.mongs.wear.data.auth.dto.request.LoginRequestDto
import com.mongs.wear.data.auth.dto.request.LogoutRequestDto
import com.mongs.wear.data.auth.dto.request.ReissueRequestDto
import com.mongs.wear.data.auth.dto.response.LoginResponseDto
import com.mongs.wear.data.auth.dto.response.ReissueResponseDto
import com.mongs.wear.data.auth.dto.request.CreateDeviceRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    /**
     * 회원 가입
     */
    @POST("auth/join")
    suspend fun join(@Body joinRequestDto: JoinRequestDto) : Response<ResponseDto<Void>>

    /**
     * 로그인
     */
    @POST("auth/login")
    suspend fun login(@Body loginRequestDto: LoginRequestDto) : Response<ResponseDto<LoginResponseDto>>

    /**
     * 토큰 재발행
     */
    @POST("auth/reissue")
    suspend fun reissue(@Body reissueRequestDto: ReissueRequestDto) : Response<ResponseDto<ReissueResponseDto>>

    /**
     * 로그 아웃
     */
    @POST("auth/logout")
    suspend fun logout(@Body logoutRequestDto: LogoutRequestDto) : Response<ResponseDto<Void>>

    /**
     * 기기 등록
     */
    @POST("auth/device")
    suspend fun createDevice(@Body createDeviceRequestDto: CreateDeviceRequestDto) : Response<ResponseDto<Void>>
}