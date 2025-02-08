package com.mongs.wear.data.user.repository

import com.mongs.wear.data.global.utils.HttpUtil
import com.mongs.wear.data.user.api.StoreApi
import com.mongs.wear.data.user.dto.request.ConsumeProductOrderRequestDto
import com.mongs.wear.data.user.dto.request.GetConsumedOrderIdsRequestDto
import com.mongs.wear.core.exception.data.ConsumeProductOrderException
import com.mongs.wear.core.exception.data.GetConsumedOrderIdsException
import com.mongs.wear.core.exception.data.GetProductIdsException
import com.mongs.wear.domain.store.repository.StoreRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepositoryImpl @Inject constructor(
    private val storeApi: StoreApi,
) : StoreRepository {

    /**
     * 인앱 상품 ID 목록 조회
     * @throws GetProductIdsException
     */
    override suspend fun getProductIds(): List<String> {

        val response = storeApi.getProducts()

        if (response.isSuccessful) {
            response.body()?.let { body ->
                return body.result.map { it.productId }
            } ?: run {
                return ArrayList()
            }
        }

        throw GetProductIdsException(result = HttpUtil.getErrorResult(response.errorBody()))
    }

    /**
     * 인앱 상품 소비 주문 ID 목록 조회
     * @throws GetConsumedOrderIdsException
     */
    override suspend fun getConsumedOrderIds(orderIds: List<String>): List<String> {

        val response = storeApi.getConsumedOrderIds(GetConsumedOrderIdsRequestDto(orderIds))

        if (response.isSuccessful) {
            response.body()?.let { body ->
                return body.result.orderIds
            } ?: run {
                return ArrayList()
            }
        }

        throw GetConsumedOrderIdsException(result = HttpUtil.getErrorResult(response.errorBody()))
    }

    /**
     * 주문 소비
     * @throws ConsumeProductOrderException
     */
    override suspend fun consumeProductOrder(productId: String, orderId: String, purchaseToken: String) {

        val response = storeApi.consumeProductOrder(
            ConsumeProductOrderRequestDto(
                productId = productId,
                orderId = orderId,
                purchaseToken = purchaseToken
            )
        )

        if (!response.isSuccessful) {
            throw ConsumeProductOrderException(result = HttpUtil.getErrorResult(response.errorBody()))
        }
    }
}