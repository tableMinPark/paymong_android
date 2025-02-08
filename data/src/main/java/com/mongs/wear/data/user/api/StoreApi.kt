package com.mongs.wear.data.user.api

import com.mongs.wear.core.dto.response.ResponseDto
import com.mongs.wear.data.user.dto.request.ConsumeProductOrderRequestDto
import com.mongs.wear.data.user.dto.request.GetConsumedOrderIdsRequestDto
import com.mongs.wear.data.user.dto.response.GetConsumedOrderIdsResponseDto
import com.mongs.wear.data.user.dto.response.GetProductsResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StoreApi {

    /**
     * 인앱 상품 목록 조회
     */
    @GET("user/store/product")
    suspend fun getProducts() : Response<ResponseDto<List<GetProductsResponseDto>>>

    /**
     * 인앱 상품 주문 ID 목록 조회
     */
    @POST("user/store/order")
    suspend fun getConsumedOrderIds(@Body consumedOrderIdsRequestDto: GetConsumedOrderIdsRequestDto) : Response<ResponseDto<GetConsumedOrderIdsResponseDto>>

    /**
     * 인앱 상품 소비
     */
    @POST("user/store/order/consume")
    suspend fun consumeProductOrder(@Body consumeProductOrderRequestDto: ConsumeProductOrderRequestDto) : Response<ResponseDto<Void>>
}