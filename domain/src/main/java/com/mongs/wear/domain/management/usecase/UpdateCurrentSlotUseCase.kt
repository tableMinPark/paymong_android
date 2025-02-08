package com.mongs.wear.domain.management.usecase

import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.UpdateCurrentSlotUseCaseException
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import com.mongs.wear.domain.auth.repository.AuthRepository
import com.mongs.wear.domain.management.repository.ManagementRepository
import com.mongs.wear.domain.management.repository.SlotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCurrentSlotUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val slotRepository: SlotRepository,
    private val managementRepository: ManagementRepository,
) : BaseNoParamUseCase<Unit>() {

    /**
     * 현재 몽 동기화 UseCase
     */
    override suspend fun execute() {
        withContext(Dispatchers.IO) {
            if (authRepository.isLogin()) {
                slotRepository.getCurrentSlot()?.let { mongModel ->
                    managementRepository.updateMong(mongId = mongModel.mongId)
                }
            }
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        throw when (exception) {
            else -> UpdateCurrentSlotUseCaseException()
        }
    }
}