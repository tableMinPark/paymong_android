package com.mongs.wear.domain.store.repository

interface StoreRepository {

    /**
     * 상품 ID 목록 조회
     * @throws GetProductIdsException
     */
    suspend fun getProductIds(): List<String>

    /**
     * 소비 상품 ID 목록 조회
     * @throws GetConsumedOrderIdsException
     */
    suspend fun getConsumedOrderIds(orderIds: List<String>): List<String>

    /**
     * 상품 주문 소비
     * @throws ConsumeProductOrderException
     */
    suspend fun consumeProductOrder(productId: String, orderId: String, purchaseToken: String)
}