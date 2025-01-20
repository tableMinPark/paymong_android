package com.mongs.wear.domain.device.exception

import com.mongs.wear.core.errors.DomainErrorCode
import com.mongs.wear.core.errors.ErrorCode
import com.mongs.wear.core.exception.UseCaseException

class SetSoundVolumeException(
    override val code: ErrorCode = DomainErrorCode.DOMAIN_DEVICE_SET_SOUND_VOLUME_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)