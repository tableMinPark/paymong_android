package com.mongs.wear.data.device.exception

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.core.errors.DataErrorCode

class ExchangeWalkingException(result: Map<String, Any> = emptyMap()) : ErrorException(
    code = DataErrorCode.DATA_USER_PLAYER_EXCHANGE_WALKING,
    result = result,
)
