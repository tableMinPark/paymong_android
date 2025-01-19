package com.mongs.wear.data.auth.exception

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.core.errors.DataErrorCode

class CreateDeviceException(result: Map<String, Any> = emptyMap()) : ErrorException(
    code = DataErrorCode.DATA_AUTH_CREATE_DEVICE,
    result = result,
)
