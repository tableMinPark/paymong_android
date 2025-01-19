package com.mongs.wear.data.device.api

import com.mongs.wear.core.dto.response.ResponseDto
import com.mongs.wear.data.device.dto.request.ExchangeWalkingCountRequestDto
import com.mongs.wear.data.device.dto.request.UpdateWalkingCountRequestDto
import com.mongs.wear.data.device.dto.response.ExchangeWalkingCountResponseDto
import com.mongs.wear.data.device.dto.response.UpdateWalkingCountResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface DeviceApi {

    @POST("user/step/exchange/walking")
    suspend fun exchangeWalkingCount(@Body exchangeWalkingCountRequestDto: ExchangeWalkingCountRequestDto) : Response<ResponseDto<ExchangeWalkingCountResponseDto>>

    @PATCH("user/step/walking")
    suspend fun updateWalkingCount(@Body updateWalkingCountRequestDto: UpdateWalkingCountRequestDto) : Response<ResponseDto<UpdateWalkingCountResponseDto>>
}