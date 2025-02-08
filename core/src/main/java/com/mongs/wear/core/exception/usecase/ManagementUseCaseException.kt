package com.mongs.wear.core.exception.usecase

import com.mongs.wear.core.errors.UseCaseErrorCode
import com.mongs.wear.core.errors.ErrorCode
import com.mongs.wear.core.exception.global.UseCaseException

class CreateMongUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_CREATE_MONG_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class DeleteMongUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_DELETE_MONG_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class EvolutionMongUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_EVOLUTION_MONG_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class FeedMongUseCaseException(
    val expirationSeconds: Long = 0,
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_FEED_MONG_FAILED,
    override val message: String = code.getMessage().format(expirationSeconds)
) : UseCaseException(code = code, message = message)

class FoodCodesEmptyUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_FOOD_CODES_EMPTY,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetCurrentSlotUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_GET_CURRENT_SLOT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetFoodCodesUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_GET_FOOD_CODES_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetSlotsUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_GET_SLOTS_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GetSnackCodesUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_SNACK_CODES_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GraduateCheckUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_GRADUATE_CHECK_MONG_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class GraduateMongUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_GRADUATE_MONG_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class PoopCleanMongUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_POOP_CLEAN_MONG_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class SetCurrentSlotUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_SET_CURRENT_SLOT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class SleepingMongUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_SLEEP_MONG_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class SnackCodesEmptyUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_SNACK_CODES_EMPTY,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)

class StrokeMongUseCaseException(
    val expirationSeconds: Long = 0,
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_STROKE_MONG_FAILED,
    override val message: String = code.getMessage().format(expirationSeconds)
) : UseCaseException(code = code, message = message)

class UpdateCurrentSlotUseCaseException(
    override val code: ErrorCode = UseCaseErrorCode.USE_CASE_MANAGEMENT_UPDATE_CURRENT_SLOT_FAILED,
    override val message: String = code.getMessage()
) : UseCaseException(code = code, message = message)