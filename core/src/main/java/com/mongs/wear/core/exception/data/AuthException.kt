package com.mongs.wear.core.exception.data

import com.mongs.wear.core.errors.DataErrorCode
import com.mongs.wear.core.exception.global.DataException

class JoinException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_AUTH_JOIN,
    result = result,
)

class LoginException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_AUTH_LOGIN,
    result = result,
)

class ReissueException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_AUTH_REISSUE,
    result = result,
)

class NeedJoinException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_AUTH_NEED_JOIN,
    result = result,
)

class NeedUpdateAppException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_AUTH_NEED_UPDATE_APP,
    result = result,
)

class LogoutException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_AUTH_LOGOUT,
    result = result,
)

class CreateDeviceException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_AUTH_CREATE_DEVICE,
    result = result,
)