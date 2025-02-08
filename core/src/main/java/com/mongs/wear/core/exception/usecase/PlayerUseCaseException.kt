package com.mongs.wear.core.exception.usecase

import com.mongs.wear.core.errors.UseCaseErrorCode
import com.mongs.wear.core.errors.ErrorCode
import com.mongs.wear.core.exception.global.UseCaseException

class BuySlotUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_PLAYER_BUY_SLOT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class ExchangeStarPointUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_PLAYER_EXCHANGE_STAR_POINT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetSlotCountUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_PLAYER_GET_SLOT_COUNT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetStarPointUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_PLAYER_GET_STAR_POINT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class UpdatePlayerUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_PLAYER_UPDATE_PLAYER_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)