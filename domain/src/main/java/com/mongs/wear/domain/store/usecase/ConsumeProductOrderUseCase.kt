package com.mongs.wear.domain.store.usecase

import com.mongs.wear.core.exception.data.ConsumeProductOrderException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.ConsumeProductOrderUseCaseException
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.domain.store.repository.StoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConsumeProductOrderUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) : BaseParamUseCase<ConsumeProductOrderUseCase.Param, Unit>() {

    /**
     * 인앱 상품 주문 소비 UseCase
     * @throws ConsumeProductOrderException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            storeRepository.consumeProductOrder(
                productId = param.productId,
                orderId = param.orderId,
                purchaseToken = param.purchaseToken
            )
        }
    }

    data class Param(

        val productId: String,

        val orderId: String,

        val purchaseToken: String,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is ConsumeProductOrderException -> throw ConsumeProductOrderUseCaseException()

            else -> throw ConsumeProductOrderUseCaseException()
        }
    }
}