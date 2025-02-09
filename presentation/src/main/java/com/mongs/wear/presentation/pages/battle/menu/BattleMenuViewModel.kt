package com.mongs.wear.presentation.pages.battle.menu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.mongs.wear.core.exception.usecase.GetBattlePayPointUseCaseException
import com.mongs.wear.core.exception.usecase.MatchWaitCancelUseCaseException
import com.mongs.wear.core.exception.usecase.MatchWaitUseCaseException
import com.mongs.wear.domain.battle.usecase.GetBattlePayPointUseCase
import com.mongs.wear.domain.battle.usecase.MatchEnterUseCase
import com.mongs.wear.domain.battle.usecase.MatchWaitCancelUseCase
import com.mongs.wear.domain.battle.usecase.MatchWaitUseCase
import com.mongs.wear.domain.battle.vo.BattleRewardVo
import com.mongs.wear.domain.battle.vo.MatchVo
import com.mongs.wear.domain.management.usecase.GetCurrentSlotUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BattleMenuViewModel @Inject constructor(
    private val getBattlePayPointUseCase: GetBattlePayPointUseCase,
    private val getCurrentSlotUseCase: GetCurrentSlotUseCase,
    private val matchWaitUseCase: MatchWaitUseCase,
    private val matchWaitCancelUseCase: MatchWaitCancelUseCase,
    private val matchEnterUseCase: MatchEnterUseCase,
) : BaseViewModel() {

    companion object {
        private const val TAG = "BattleMenuViewModel"
    }

    private val _matchVo = MediatorLiveData<MatchVo?>(null)
    val matchVo: LiveData<MatchVo?> get() = _matchVo

    private val _payPoint = MediatorLiveData<Int>(0)
    val payPoint: LiveData<Int> get() = _payPoint

    private val _battleRewardVo = MediatorLiveData<BattleRewardVo?>(null)
    val battleRewardVo: LiveData<BattleRewardVo?> get() = _battleRewardVo

    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {

            uiState.loadingBar = true

            _battleRewardVo.postValue(getBattlePayPointUseCase())

            _payPoint.addSource(withContext(Dispatchers.IO) { getCurrentSlotUseCase() }) {
                it?.let { mongVo ->
                    _payPoint.value = mongVo.payPoint
                } ?: run { _payPoint.value = 0 }
            }

            uiState.loadingBar = false
        }
    }

    /**
     * 매칭 대기열 등록
     */
    fun createMatchWait() {
        viewModelScopeWithHandler.launch (Dispatchers.Main) {

            uiState.loadingBar = true

            _matchVo.addSource(withContext(Dispatchers.IO) { matchWaitUseCase() }) { matchVo ->
                _matchVo.value = matchVo
            }
        }
    }

    /**
     * 매칭 대기열 취소
     */
    fun matchWaitCancel() {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {
            _matchVo.postValue(null)
            matchWaitCancelUseCase()
            uiState.loadingBar = false
        }
    }

    /**
     * 매칭 입장
     */
    fun matchEnter(roomId: Long) {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {
            matchEnterUseCase(
                MatchEnterUseCase.Param(
                    roomId = roomId,
                )
            )
        }
    }

    /**
     * 매칭 퇴장
     */
    fun matchExit() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _matchVo.postValue(null)
                matchWaitCancelUseCase()
            } catch (exception: Exception) {
                Log.w(TAG, "[WARN] ${exception.javaClass.simpleName} ${exception.message}")
            } finally {
                uiState.loadingBar = false
            }
        }
    }

    val uiState: UiState = UiState()

    class UiState : BaseUiState() {
        var navMainEvent = MutableSharedFlow<Long>()
    }

    override suspend fun exceptionHandler(exception: Throwable) {
        when (exception) {

            is GetBattlePayPointUseCaseException -> {
                uiState.navMainEvent.emit(System.currentTimeMillis())
            }

            is MatchWaitUseCaseException -> {
                matchExit()
            }

            is MatchWaitCancelUseCaseException -> {
                uiState.navMainEvent.emit(System.currentTimeMillis())
            }

            else -> {
                matchExit()
            }
        }
    }
}