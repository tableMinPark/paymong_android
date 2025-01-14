package com.mongs.wear.presentation.pages.battle.menu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.battle.exception.MatchWaitException
import com.mongs.wear.domain.battle.usecase.MatchWaitCancelUseCase
import com.mongs.wear.domain.battle.usecase.MatchWaitUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BattleMenuViewModel @Inject constructor(
    private val matchWaitUseCase: MatchWaitUseCase,
    private val matchWaitCancelUseCase: MatchWaitCancelUseCase,
): BaseViewModel() {

    fun createMatchWait() {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {

            uiState.loadingBar = true

            matchWaitUseCase(
                MatchWaitUseCase.Param (
                    matchFindCallback = {
                        uiState.isMatchWait = false
                    },
                    matchEnterCallback = {
                        uiState.navBattleMatchEvent.emit(System.currentTimeMillis())
                    }
                )
            )
        }
    }

    fun matchWaitCancel() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                matchWaitCancelUseCase()
                uiState.loadingBar = false
            } catch (_: ErrorException) {
                uiState.loadingBar = false
            }
        }
    }

    val uiState: UiState = UiState()

    class UiState : BaseUiState() {
        var navBattleMatchEvent = MutableSharedFlow<Long>()
        var isMatchWait by mutableStateOf(false)
    }

    override fun exceptionHandler(exception: Throwable) {

        when (exception) {

            is MatchWaitException -> {
                uiState.loadingBar = false
            }

            else -> {
                uiState.loadingBar = false
            }
        }
    }
}