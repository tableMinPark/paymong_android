package com.mongs.wear.domain.device.exception

import com.mongs.wear.core.exception.UseCaseException
import com.mongs.wear.core.errors.DomainErrorCode
import com.mongs.wear.core.errors.ErrorCode

class DisConnectMqttException(
    override val code: ErrorCode = DomainErrorCode.DOMAIN_DEVICE_DISCONNECT_MQTT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)