package com.mongs.wear.core.exception.usecase

import com.mongs.wear.core.errors.UseCaseErrorCode
import com.mongs.wear.core.errors.ErrorCode
import com.mongs.wear.core.exception.global.UseCaseException

class GetMapCollectionsUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_COLLECTION_GET_MAP_COLLECTIONS_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetMongCollectionUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_COLLECTION_GET_MONG_COLLECTIONS_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)
