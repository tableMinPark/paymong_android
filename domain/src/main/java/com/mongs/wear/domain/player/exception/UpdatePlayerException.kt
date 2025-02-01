package com.mongs.wear.domain.player.exception

import com.mongs.wear.core.exception.UseCaseException
import com.mongs.wear.core.errors.DomainErrorCode
import com.mongs.wear.core.errors.ErrorCode

class UpdatePlayerException(
    override val code: ErrorCode = DomainErrorCode.DOMAIN_PLAYER_UPDATE_PLAYER_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)