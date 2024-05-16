package com.paymong.wear.domain.usecase.feed

import com.paymong.wear.domain.repositroy.CodeRepository
import com.paymong.wear.domain.repositroy.ManagementRepository
import com.paymong.wear.domain.repositroy.SlotRepository
import com.paymong.wear.domain.vo.FoodVo
import javax.inject.Inject

class GetFoodCodesUseCase @Inject constructor(
    private val codeRepository: CodeRepository,
    private val slotRepository: SlotRepository,
    private val managementRepository: ManagementRepository,
) {
    suspend operator fun invoke(): List<FoodVo> {
        val slotModel = slotRepository.getNowSlot()
        val mongId = slotModel.mongId

        val foodVos = ArrayList<FoodVo>()
        val feedLogModelMap = managementRepository.getFeedLog(mongId = mongId).associateBy { it.code }

        codeRepository.getFoodCodes().forEach {
            val feedLog = feedLogModelMap[it.code]!!

            foodVos.add(
                FoodVo(
                    code = it.code,
                    name = it.name,
                    price = it.price,
                    addWeight = it.addWeight,
                    addStrength = it.addStrength,
                    addSatiety = it.addSatiety,
                    addHealthy = it.addHealthy,
                    addSleep = it.addSleep,
                    isCanBuy = feedLog.isCanBuy
                )
            )
        }
        return foodVos
    }
}