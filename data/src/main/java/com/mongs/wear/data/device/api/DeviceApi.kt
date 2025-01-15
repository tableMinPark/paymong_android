package com.mongs.wear.data.device.api

import com.mongs.wear.core.dto.response.ResponseDto
import com.mongs.wear.data.device.dto.request.CreateDeviceRequestDto
import com.mongs.wear.data.device.dto.request.ExchangeWalkingCountRequestDto
import com.mongs.wear.data.device.dto.request.UpdateWalkingCountRequestDto
import com.mongs.wear.data.device.dto.response.ExchangeWalkingCountResponseDto
import com.mongs.wear.data.device.dto.response.UpdateWalkingCountResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface DeviceApi {

    @POST("user/device/exchange/walking")
    suspend fun exchangeWalkingCount(@Body exchangeWalkingCountRequestDto: ExchangeWalkingCountRequestDto) : Response<ResponseDto<ExchangeWalkingCountResponseDto>>

    @POST("user/open/device")
    suspend fun createDevice(@Body createDeviceRequestDto: CreateDeviceRequestDto) : Response<ResponseDto<Void>>

    @PATCH("user/open/device/walking")
    suspend fun updateWalkingCount(@Body updateWalkingCountRequestDto: UpdateWalkingCountRequestDto) : Response<ResponseDto<UpdateWalkingCountResponseDto>>
}