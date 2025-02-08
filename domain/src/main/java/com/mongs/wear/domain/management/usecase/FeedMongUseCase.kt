package com.mongs.wear.domain.management.usecase

import com.mongs.wear.core.errors.DataErrorCode
import com.mongs.wear.core.exception.data.FeedMongException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.core.exception.usecase.FeedMongUseCaseException
import com.mongs.wear.domain.management.repository.ManagementRepository
import com.mongs.wear.domain.management.repository.SlotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FeedMongUseCase @Inject constructor(
    private val slotRepository: SlotRepository,
    private val managementRepository: ManagementRepository,
) : BaseParamUseCase<FeedMongUseCase.Param, Unit>() {

    /**
     * 몽 먹이 주기 UseCase
     * @throws FeedMongException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            slotRepository.getCurrentSlot()?.let { slotModel ->
                managementRepository.feedMong(mongId = slotModel.mongId, foodTypeCode = param.foodTypeCode)
            } ?: run {  }
        }
    }

    data class Param(
        val foodTypeCode: String,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        throw when (exception) {
            is FeedMongException -> {

                val expirationSeconds = exception.result["expirationSeconds"]?.toString()
                    ?.toDouble()?.toLong()
                    ?: 0

                throw FeedMongUseCaseException(expirationSeconds = expirationSeconds)
            }

            else -> FeedMongUseCaseException()
        }
    }
}