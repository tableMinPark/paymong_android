package com.mongs.wear.core.exception.data

import com.mongs.wear.core.errors.DataErrorCode
import com.mongs.wear.core.exception.global.DataException

class CreateMongException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_MANAGER_MANAGEMENT_CREATE_MONG,
    result = result,
)

class DeleteMongException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_MANAGER_MANAGEMENT_DELETE_MONG,
    result = result,
)

class StrokeMongException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_MANAGER_MANAGEMENT_STROKE_MONG,
    result = result,
)

class PoopCleanMongException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_MANAGER_MANAGEMENT_POOP_CLEAN_MONG,
    result = result,
)

class SleepMongException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_MANAGER_MANAGEMENT_SLEEP_MONG,
    result = result,
)

class GetFeedItemsException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_MANAGER_MANAGEMENT_GET_FEED_ITEMS,
    result = result,
)

class FeedMongException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_MANAGER_MANAGEMENT_FEED_MONG,
    result = result,
)

class EvolutionMongException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_MANAGER_MANAGEMENT_EVOLUTION_MONG,
    result = result,
)

class GraduateMongException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_MANAGER_MANAGEMENT_GRADUATE_MONG,
    result = result,
)