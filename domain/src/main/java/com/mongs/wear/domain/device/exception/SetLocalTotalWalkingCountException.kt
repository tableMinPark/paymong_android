package com.mongs.wear.domain.device.exception

import com.mongs.wear.core.exception.UseCaseException
import com.mongs.wear.core.errors.DomainErrorCode
import com.mongs.wear.core.errors.ErrorCode

class SetLocalTotalWalkingCountException(
    override val code: ErrorCode = DomainErrorCode.DOMAIN_PLAYER_SET_LOCAL_TOTAL_WALKING_COUNT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)