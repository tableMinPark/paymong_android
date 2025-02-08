package com.mongs.wear.core.exception.usecase

import com.mongs.wear.core.errors.UseCaseErrorCode
import com.mongs.wear.core.errors.ErrorCode
import com.mongs.wear.core.exception.global.UseCaseException

class CreateDeviceUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_AUTH_CREATE_DEVICE_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class JoinUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_AUTH_JOIN_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class LoginUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_AUTH_LOGIN_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class LogoutUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_AUTH_LOGOUT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class NeedJoinUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_AUTH_NEED_JOIN_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class NeedUpdateAppUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_AUTH_NEED_UPDATE_APP_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)