package com.mongs.wear.core.exception.data

import com.mongs.wear.core.errors.DataErrorCode
import com.mongs.wear.core.exception.global.DataException

class GetMapCollectionsException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_USER_COLLECTION_GET_MAP_COLLECTIONS,
    result = result,
)

class GetMongCollectionsException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_USER_COLLECTION_GET_MONG_COLLECTIONS,
    result = result,
)

class CreatePlayerException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_USER_PLAYER_CREATE_PLAYER,
    result = result,
)

class GetPlayerException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_USER_PLAYER_GET_PLAYER,
    result = result,
)

class BuySlotException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_USER_PLAYER_BUY_SLOT,
    result = result,
)

class ExchangeStarPointException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_USER_PLAYER_EXCHANGE_STAR_POINT,
    result = result,
)

class CreateFeedbackException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_USER_FEEDBACK_CREATE_FEEDBACK,
    result = result,
)

class GetProductIdsException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_USER_STORE_GET_PRODUCT_IDS,
    result = result,
)

class GetConsumedOrderIdsException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_USER_STORE_CONSUMED_ORDER_IDS,
    result = result,
)

class ConsumeProductOrderException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_USER_STORE_CONSUME_PRODUCT_ORDER,
    result = result,
)
