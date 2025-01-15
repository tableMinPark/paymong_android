package com.mongs.wear.domain.device.exception

import com.mongs.wear.core.exception.UseCaseException
import com.mongs.wear.core.errors.DomainErrorCode
import com.mongs.wear.core.errors.ErrorCode

class UpdateTotalWalkingCountException(
    override val code: ErrorCode = DomainErrorCode.DOMAIN_PLAYER_UPDATE_TOTAL_WALKING_COUNT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)