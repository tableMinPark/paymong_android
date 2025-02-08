package com.mongs.wear.core.exception.usecase

import com.mongs.wear.core.errors.UseCaseErrorCode
import com.mongs.wear.core.errors.ErrorCode
import com.mongs.wear.core.exception.global.UseCaseException


class ConnectMqttUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_GLOBAL_CONNECT_MQTT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class DisConnectMqttUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_GLOBAL_DISCONNECT_MQTT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class PauseConnectMqttUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_GLOBAL_PAUSE_CONNECT_MQTT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)