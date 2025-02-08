package com.mongs.wear.core.exception.usecase

import com.mongs.wear.core.errors.UseCaseErrorCode
import com.mongs.wear.core.errors.ErrorCode
import com.mongs.wear.core.exception.global.UseCaseException

class CreateFeedbackUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_FEEDBACK_CREATE_FEEDBACK_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)