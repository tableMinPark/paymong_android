package com.mongs.wear.data.user.api

import com.mongs.wear.core.dto.response.ResponseDto
import com.mongs.wear.data.user.dto.request.ExchangeStarPointRequestDto
import com.mongs.wear.data.user.dto.response.GetPlayerResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface PlayerApi {

    /**
     * 플레이어 정보 등록
     */
    @POST("user/player")
    suspend fun createPlayer() : Response<ResponseDto<Void>>

    /**
     * 플레이어 정보 조회
     */
    @GET("user/player")
    suspend fun getPlayer() : Response<ResponseDto<GetPlayerResponseDto>>

    /**
     * 새로운 슬롯 구매
     */
    @PATCH("user/player/slot")
    suspend fun buySlot() : Response<ResponseDto<Void>>

    /**
     * 스타 포인트 환전
     */
    @POST("user/player/exchange/starPoint")
    suspend fun exchangeStarPoint(@Body exchangeStarPointRequestDto: ExchangeStarPointRequestDto) : Response<ResponseDto<Void>>
}