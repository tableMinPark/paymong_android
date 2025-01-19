package com.mongs.wear.domain.device.exception

import com.mongs.wear.core.exception.UseCaseException
import com.mongs.wear.core.errors.DomainErrorCode
import com.mongs.wear.core.errors.ErrorCode

class SetDeviceIdException(
    override val code: ErrorCode = DomainErrorCode.DOMAIN_DEVICE_SET_DEVICE_ID_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)