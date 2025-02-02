package com.mongs.wear.domain.global.exception

import com.mongs.wear.core.exception.UseCaseException
import com.mongs.wear.core.errors.DomainErrorCode
import com.mongs.wear.core.errors.ErrorCode

class PauseConnectMqttException(
    override val code: ErrorCode = DomainErrorCode.DOMAIN_GLOBAL_PAUSE_CONNECT_MQTT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)