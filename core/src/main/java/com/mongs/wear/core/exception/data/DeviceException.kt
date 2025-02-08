package com.mongs.wear.core.exception.data

import com.mongs.wear.core.errors.DataErrorCode
import com.mongs.wear.core.exception.global.DataException

class ExchangeWalkingException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_USER_DEVICE_EXCHANGE_WALKING,
    result = result,
)
