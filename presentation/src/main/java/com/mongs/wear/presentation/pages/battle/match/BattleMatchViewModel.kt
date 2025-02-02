package com.mongs.wear.presentation.pages.battle.match

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.mongs.wear.core.enums.MatchRoundCode
import com.mongs.wear.domain.battle.exception.NotExistsPlayerIdException
import com.mongs.wear.domain.battle.exception.NotExistsTargetPlayerIdException
import com.mongs.wear.domain.battle.usecase.GetBattlePayPointUseCase
import com.mongs.wear.domain.battle.usecase.GetMatchUseCase
import com.mongs.wear.domain.battle.usecase.GetMyMatchPlayerUseCase
import com.mongs.wear.domain.battle.usecase.GetRiverMatchPlayerUseCase
import com.mongs.wear.domain.battle.usecase.MatchEndUseCase
import com.mongs.wear.domain.battle.usecase.MatchExitUseCase
import com.mongs.wear.domain.battle.usecase.MatchOverUseCase
import com.mongs.wear.domain.battle.usecase.MatchPickUseCase
import com.mongs.wear.domain.battle.usecase.MatchStartUseCase
import com.mongs.wear.domain.battle.vo.MatchPlayerVo
import com.mongs.wear.domain.battle.vo.MatchVo
import com.mongs.wear.domain.device.usecase.GetNetworkUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BattleMatchViewModel @Inject constructor(
    private val getNetworkUseCase: GetNetworkUseCase,
    private val getBattlePayPointUseCase: GetBattlePayPointUseCase,
    private val getMatchUseCase: GetMatchUseCase,
    private val getMyMatchPlayerUseCase: GetMyMatchPlayerUseCase,
    private val getRiverMatchPlayerUseCase: GetRiverMatchPlayerUseCase,
    private val matchStartUseCase: MatchStartUseCase,
    private val matchPickUseCase: MatchPickUseCase,
    private val matchOverUseCase: MatchOverUseCase,
    private val matchExitUseCase: MatchExitUseCase,
    private val matchEndUseCase: MatchEndUseCase,
): BaseViewModel() {

    companion object {
        private const val TAG = "BattleMatchViewModel"
    }

    val network: LiveData<Boolean> get() = _network
    private val _network = MediatorLiveData(true)

    private val _battlePayPoint = MediatorLiveData<Int>(0)
    val battlePayPoint: LiveData<Int> get() = _battlePayPoint

    private val _matchVo = MediatorLiveData<MatchVo?>()
    val matchVo: LiveData<MatchVo?> get() = _matchVo

    private val _myMatchPlayerVo = MediatorLiveData<MatchPlayerVo?>()
    val myMatchPlayerVo: LiveData<MatchPlayerVo?> get() = _myMatchPlayerVo

    private val _otherMatchPlayerVo = MediatorLiveData<MatchPlayerVo?>()
    val otherMatchPlayerVo: LiveData<MatchPlayerVo?> get() = _otherMatchPlayerVo

    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {

            uiState.loadingBar = true

            // network flag 옵저버 객체 조회
            _network.addSource(withContext(Dispatchers.IO) { getNetworkUseCase() }) {
                _network.value = it
            }

            _battlePayPoint.postValue(getBattlePayPointUseCase())

            _matchVo.addSource(withContext(Dispatchers.IO) { getMatchUseCase() }) { matchVo ->
                _matchVo.value = matchVo
            }

            _myMatchPlayerVo.addSource(withContext(Dispatchers.IO) { getMyMatchPlayerUseCase() }) { myMatchPlayerVo ->
                _myMatchPlayerVo.value = myMatchPlayerVo
            }

            _otherMatchPlayerVo.addSource(withContext(Dispatchers.IO) { getRiverMatchPlayerUseCase() }) { otherMatchPlayerVo ->
                _otherMatchPlayerVo.value = otherMatchPlayerVo
            }

            uiState.loadingBar = false
        }
    }

    /**
     * 배틀 매치 시작
     */
    fun matchStart(roomId: Long) {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {

            delay(3000)

            matchStartUseCase(
                MatchStartUseCase.Param(
                    roomId = roomId,
                )
            )
        }
    }

    /**
     * 다음 라운드 진행
     */
    fun nextRound() {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {

            delay(3000)

            uiState.matchPickDialog = true
        }
    }

    /**
     * 배틀 매치 선택
     */
    fun matchPick(roomId: Long, playerId: String?, targetPlayerId: String?, pickCode: MatchRoundCode) {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {

            matchPickUseCase(
                MatchPickUseCase.Param(
                    roomId = roomId,
                    playerId = playerId,
                    targetPlayerId = targetPlayerId,
                    pickCode = pickCode,
                )
            )

            uiState.matchPickDialog = false
        }
    }

    /**
     * 매치 종료
     */
    fun matchOver(roomId: Long) {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {

            delay(3000)

            uiState.matchPickDialog = false
            uiState.matchOverLoadingBar = true

            delay(1000)

            // 매치 결과 정보 업데이트
            matchOverUseCase(
                MatchOverUseCase.Param(
                    roomId = roomId,
                )
            )

            uiState.matchOverLoadingBar = false
            uiState.matchOverDialog = true
        }
    }

    /**
     * 매치 퇴장
     */
    fun matchExit(roomId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _matchVo.postValue(null)

                matchExitUseCase(
                    MatchExitUseCase.Param(
                        roomId = roomId
                    )
                )

                matchEndUseCase()

            } catch (exception: Exception) {
                Log.w(TAG, "[WARN] ${exception.javaClass.simpleName} ${exception.message}")
            } finally {
                uiState.loadingBar = false
                uiState.matchOverLoadingBar = false
                uiState.matchPickDialog = false
                uiState.navMainEvent.emit(System.currentTimeMillis())
            }
        }
    }

    /**
     * 매치 종료
     */
    fun matchEnd() {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {

            uiState.matchOverLoadingBar = false
            uiState.matchPickDialog = false

            matchEndUseCase()

            uiState.navMainEvent.emit(System.currentTimeMillis())
        }
    }

    val uiState: UiState = UiState()

    class UiState : BaseUiState() {
        var navMainEvent = MutableSharedFlow<Long>()
        var matchOverLoadingBar by mutableStateOf(false)
        var matchPickDialog by mutableStateOf(false)
        var matchOverDialog by mutableStateOf(false)
    }

    override fun exceptionHandler(exception: Throwable) {

        when (exception) {

            is NotExistsPlayerIdException -> {}

            is NotExistsTargetPlayerIdException -> {}

            else -> {
                _matchVo.value?.let {
                    matchExit(roomId = it.roomId)
                }
            }
        }
    }
}