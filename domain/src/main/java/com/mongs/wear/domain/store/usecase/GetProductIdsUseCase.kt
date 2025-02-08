package com.mongs.wear.domain.store.usecase

import com.mongs.wear.core.exception.data.GetProductIdsException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.GetProductIdsUseCaseException
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import com.mongs.wear.domain.store.repository.StoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProductIdsUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) : BaseNoParamUseCase<List<String>>() {

    /**
     * 인앱 상품 ID 목록 조회 UseCase
     * @throws GetProductIdsException
     */
    override suspend fun execute(): List<String> {
        return withContext(Dispatchers.IO) {
            storeRepository.getProductIds().map { productId ->
                // 공통 코드 (대문자) -> 구글 사용 코드 (소문자) 변환
                productId.lowercase()
            }
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is GetProductIdsException -> throw GetProductIdsUseCaseException()

            else -> throw GetProductIdsUseCaseException()
        }
    }
}