package com.mongs.wear.core.exception.presentation

import com.mongs.wear.core.errors.ErrorCode
import com.mongs.wear.core.errors.PresentationErrorCode
import com.mongs.wear.core.exception.global.PresentationException

class NotExistsEmailException(
    override val code: ErrorCode = PresentationErrorCode.PRESENTATION_AUTH_NOT_EXISTS_EMAIL,
    override val message: String = code.getMessage()
) : PresentationException(code = code, message = message)

class NotExistsNameException(
    override val code: ErrorCode = PresentationErrorCode.PRESENTATION_AUTH_NOT_EXISTS_NAME,
    override val message: String = code.getMessage()
) : PresentationException(code = code, message = message)

class NotExistsGoogleAccountIdException(
    override val code: ErrorCode = PresentationErrorCode.PRESENTATION_AUTH_NOT_EXISTS_GOOGLE_ACCOUNT_ID,
    override val message: String = code.getMessage()
) : PresentationException(code = code, message = message)