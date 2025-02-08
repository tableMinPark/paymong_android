package com.mongs.wear.core.exception.data

import com.mongs.wear.core.errors.DataErrorCode
import com.mongs.wear.core.exception.global.DataException

class GetBattleException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_ACTIVITY_BATTLE_GET_BATTLE,
    result = result,
)

class GetBattleRewardException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_ACTIVITY_BATTLE_GET_BATTLE_REWARD,
    result = result,
)

class NotExistsMatchException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_ACTIVITY_BATTLE_NOT_EXISTS_MATCH,
    result = result,
)

class NotExistsMatchPlayerException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_ACTIVITY_BATTLE_NOT_EXISTS_MATCH_PLAYER,
    result = result,
)

class UpdateOverMatchException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_ACTIVITY_BATTLE_UPDATE_OVER_MATCH,
    result = result,
)

class CreateMatchException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_ACTIVITY_BATTLE_CREATE_MATCH,
    result = result,
)

class DeleteMatchException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_ACTIVITY_BATTLE_DELETE_MATCH,
    result = result,
)

class EnterMatchException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_ACTIVITY_BATTLE_ENTER_MATCH,
    result = result,
)

class ExitMatchException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_ACTIVITY_BATTLE_EXIT_MATCH,
    result = result,
)

class PickMatchException(result: Map<String, Any> = emptyMap()) : DataException(
    code = DataErrorCode.DATA_ACTIVITY_BATTLE_PICK_MATCH,
    result = result,
)