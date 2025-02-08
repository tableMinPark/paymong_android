package com.mongs.wear.domain.management.usecase

import com.mongs.wear.core.exception.data.GetFeedItemsException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import com.mongs.wear.core.exception.usecase.FoodCodesEmptyUseCaseException
import com.mongs.wear.core.exception.usecase.GetFoodCodesUseCaseException
import com.mongs.wear.domain.management.repository.ManagementRepository
import com.mongs.wear.domain.management.repository.SlotRepository
import com.mongs.wear.domain.management.vo.FeedItemVo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetFoodCodesUseCase @Inject constructor(
    private val slotRepository: SlotRepository,
    private val managementRepository: ManagementRepository,
) : BaseNoParamUseCase<List<FeedItemVo>>() {

    companion object {
        private const val FOOD_TYPE_GROUP_CODE = "FD"
    }

    /**
     * 먹이 목록 조회 UseCase
     * @throws GetFeedItemsException
     */
    override suspend fun execute(): List<FeedItemVo> {
        return withContext(Dispatchers.IO) {
            val feedItemVoList = slotRepository.getCurrentSlot()?.let { slotModel ->
                managementRepository.getFeedItems(mongId = slotModel.mongId, foodTypeGroupCode = FOOD_TYPE_GROUP_CODE).map {
                    FeedItemVo(
                        foodTypeCode = it.foodTypeCode,
                        foodTypeName = it.foodTypeName,
                        price = it.price,
                        isCanBuy = it.isCanBuy,
                        addWeightValue = it.addWeightValue,
                        addStrengthValue = it.addStrengthValue,
                        addSatietyValue = it.addSatietyValue,
                        addHealthyValue = it.addHealthyValue,
                        addFatigueValue = it.addFatigueValue,
                    )
                }
            } ?: run { ArrayList() }

            if (feedItemVoList.isEmpty()) throw FoodCodesEmptyUseCaseException()

            feedItemVoList
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is GetFeedItemsException -> throw GetFoodCodesUseCaseException()

            else -> throw GetFoodCodesUseCaseException()
        }
    }
}