package com.mongs.wear.domain.management.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.auth.repository.AuthRepository
import com.mongs.wear.domain.global.usecase.BaseNoParamUseCase
import com.mongs.wear.domain.management.exception.UpdateCurrentSlotException
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

    override suspend fun execute() {

        withContext(Dispatchers.IO) {
            if (authRepository.isLogin()) {
                slotRepository.getCurrentSlot()?.let { mongModel ->
                    managementRepository.updateMong(mongId = mongModel.mongId)
                }
            }
        }
    }

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        throw when (exception.code) {
            else -> UpdateCurrentSlotException()
        }
    }
}