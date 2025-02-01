package com.mongs.wear.domain.device.exception

import com.mongs.wear.core.errors.DomainErrorCode
import com.mongs.wear.core.errors.ErrorCode
import com.mongs.wear.core.exception.UseCaseException

class SetServerTotalWalkingCountException(
    override val code: ErrorCode = DomainErrorCode.DOMAIN_PLAYER_SET_SERVER_TOTAL_WALKING_COUNT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)