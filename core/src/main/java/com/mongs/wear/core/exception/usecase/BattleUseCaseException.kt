package com.mongs.wear.core.exception.usecase

import com.mongs.wear.core.errors.UseCaseErrorCode
import com.mongs.wear.core.errors.ErrorCode
import com.mongs.wear.core.exception.global.UseCaseException

class GetBattlePayPointUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_GET_BATTLE_PAY_POINT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetMatchUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_BATTLE_GET_MATCH_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetMyMatchUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_BATTLE_GET_MY_MATCH_PLAYER_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetRiverMatchPlayerUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_BATTLE_GET_RIVER_MATCH_PLAYER_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class MatchEndUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_BATTLE_MATCH_END_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class MatchEnterUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_BATTLE_MATCH_ENTER_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class MatchExitUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_BATTLE_MATCH_EXIT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class MatchOverUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_BATTLE_OVER_MATCH_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class MatchPickUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_BATTLE_PICK_MATCH_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class MatchStartUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_BATTLE_MATCH_START_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class MatchWaitCancelUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_BATTLE_MATCH_WAIT_CANCEL_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class MatchWaitUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_BATTLE_MATCH_WAIT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class NotExistsPlayerIdUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_BATTLE_NOT_EXISTS_PLAYER_ID_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class NotExistsTargetPlayerIdUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_BATTLE_NOT_EXISTS_TARGET_PLAYER_ID_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class UpdateMatchUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_BATTLE_UPDATE_MATCH_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)