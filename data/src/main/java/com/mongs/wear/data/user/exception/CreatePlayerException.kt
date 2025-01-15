package com.mongs.wear.data.user.exception

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.core.errors.DataErrorCode
import java.util.Collections

class CreatePlayerException(result: Map<String, Any> = emptyMap()) : ErrorException(
    code = DataErrorCode.DATA_USER_PLAYER_CREATE_PLAYER,
    result = result,
)
