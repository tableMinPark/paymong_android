package com.mongs.wear.domain.management.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.global.usecase.BaseNoParamUseCase
import com.mongs.wear.domain.management.exception.GetSlotsException
import com.mongs.wear.domain.management.repository.ManagementRepository
import com.mongs.wear.domain.management.vo.SlotVo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSlotsUseCase @Inject constructor(
    private val managementRepository: ManagementRepository,
) : BaseNoParamUseCase<List<SlotVo>>() {

    override suspend fun execute(): List<SlotVo> {

        return withContext(Dispatchers.IO) {
            // 몽 정보 전체 동기화
            managementRepository.updateMongs()

            // 몽 정보 전체 조회
            managementRepository.getMongs().map { mongModel ->
                SlotVo(
                    code = SlotVo.SlotCode.EXISTS,
                    mongVo = mongModel.toMongVo()
                )
            }
        }
    }

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw GetSlotsException()
        }
    }
}