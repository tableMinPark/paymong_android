package com.mongs.wear.domain.store.usecase

import com.mongs.wear.core.exception.data.GetConsumedOrderIdsException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.GetConsumedOrderIdsUseCaseException
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.domain.store.repository.StoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetConsumedOrderIdsUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) : BaseParamUseCase<GetConsumedOrderIdsUseCase.Param, List<String>>() {

    /**
     * 소비 주문 ID 목록 조회 UseCase
     * @throws GetConsumedOrderIdsException
     */
    override suspend fun execute(param: Param): List<String> {
        return withContext(Dispatchers.IO) {
            storeRepository.getConsumedOrderIds(
                orderIds = param.orderIds,
            )
        }
    }

    data class Param(
        val orderIds: List<String>,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is GetConsumedOrderIdsException -> throw GetConsumedOrderIdsUseCaseException()

            else -> throw GetConsumedOrderIdsUseCaseException()
        }
    }
}