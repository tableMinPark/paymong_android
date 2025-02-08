package com.mongs.wear.core.exception.data

import com.mongs.wear.core.errors.DataErrorCode
import com.mongs.wear.core.exception.global.DataException

class ConnectMqttException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_GLOBAL_MQTT_CONNECT,
    result = result,
)

class DisconnectMqttException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_GLOBAL_MQTT_DISCONNECT,
    result = result,
)

class DisSubMqttException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_GLOBAL_MQTT_DIS_SUB,
    result = result,
)

class PauseMqttException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_GLOBAL_MQTT_PAUSE,
    result = result,
)

class SubMqttException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_GLOBAL_MQTT_SUB,
    result = result,
)

class PubMqttException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_GLOBAL_MQTT_PUB,
    result = result,
)