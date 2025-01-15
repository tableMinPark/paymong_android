package com.mongs.wear.domain.device.exception

import com.mongs.wear.core.errors.DomainErrorCode
import com.mongs.wear.core.errors.ErrorCode
import com.mongs.wear.core.exception.UseCaseException

class CreateDeviceException(
    override val code: ErrorCode = DomainErrorCode.DOMAIN_PLAYER_CREATE_DEVICE_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)