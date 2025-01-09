package com.mongs.wear.domain.battle.exception

import com.mongs.wear.core.exception.UseCaseException
import com.mongs.wear.core.errors.DomainErrorCode
import com.mongs.wear.core.errors.ErrorCode

class MatchWaitCancelException(
    override val code: ErrorCode = DomainErrorCode.DOMAIN_BATTLE_MATCH_WAIT_CANCEL_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)