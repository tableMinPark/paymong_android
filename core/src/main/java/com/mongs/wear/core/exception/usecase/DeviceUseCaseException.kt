package com.mongs.wear.core.exception.usecase

import com.mongs.wear.core.errors.UseCaseErrorCode
import com.mongs.wear.core.errors.ErrorCode
import com.mongs.wear.core.exception.global.UseCaseException

class ExchangeWalkingCountUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_PLAYER_EXCHANGE_WALKING_COUNT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetBackgroundMapCodeUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_DEVICE_GET_BACKGROUND_MAP_CODE_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetNetworkUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_DEVICE_GET_NETWORK_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetNotificationUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_DEVICE_GET_NOTIFICATION_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetSoundVolumeUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_DEVICE_GET_SOUND_VOLUME_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetStepsUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_PLAYER_GET_STEPS_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class SetBackgroundMapCodeUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_DEVICE_SET_BACKGROUND_MAP_CODE_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class SetDeviceIdUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_DEVICE_SET_DEVICE_ID_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class SetLocalTotalWalkingCountUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_PLAYER_SET_LOCAL_TOTAL_WALKING_COUNT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class SetNetworkUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_DEVICE_SET_NETWORK_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class SetNotificationUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_DEVICE_SET_NOTIFICATION_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class SetServerTotalWalkingCountUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_PLAYER_SET_SERVER_TOTAL_WALKING_COUNT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class SetSoundVolumeUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_DEVICE_SET_SOUND_VOLUME_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)