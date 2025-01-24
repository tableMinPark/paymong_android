package com.mongs.wear.domain.training.exception

import com.mongs.wear.core.exception.UseCaseException
import com.mongs.wear.core.errors.DomainErrorCode
import com.mongs.wear.core.errors.ErrorCode

class GetTrainingPayPointException(
    override val code: ErrorCode = DomainErrorCode.DOMAIN_GET_TRAINING_PAY_POINT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)