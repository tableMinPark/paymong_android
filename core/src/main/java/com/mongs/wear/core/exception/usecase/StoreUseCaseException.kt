package com.mongs.wear.core.exception.usecase

import com.mongs.wear.core.errors.UseCaseErrorCode
import com.mongs.wear.core.errors.ErrorCode
import com.mongs.wear.core.exception.global.UseCaseException

class ConsumeProductOrderUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_STORE_CONSUME_PRODUCT_ORDER_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(
    code = UseCaseErrorCode.USE_CASE_STORE_CONSUME_PRODUCT_ORDER_FAILED,
)

class GetConsumedOrderIdsUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_STORE_GET_CONSUMED_ORDER_IDS_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetProductIdsUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_STORE_GET_PRODUCT_IDS_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)