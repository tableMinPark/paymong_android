package com.mongs.wear.data.auth.exception

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.core.errors.DataErrorCode
import java.util.Collections

class JoinException(result: Map<String, Any>) : ErrorException(
    code = DataErrorCode.DATA_AUTH_JOIN,
    result = result,
)
