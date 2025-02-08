package com.mongs.wear.domain.management.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.GetCurrentSlotUseCaseException
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import com.mongs.wear.domain.management.repository.SlotRepository
import com.mongs.wear.domain.management.vo.MongVo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCurrentSlotUseCase @Inject constructor(
    private val slotRepository: SlotRepository,
) : BaseNoParamUseCase<LiveData<MongVo?>>() {

    /**
     * 현재 슬롯 조회 UseCase
     */
    override suspend fun execute(): LiveData<MongVo?> {
        return withContext(Dispatchers.IO) {
            slotRepository.getCurrentSlotLive().map { it?.toMongVo() }
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            else -> throw GetCurrentSlotUseCaseException()
        }
    }
}