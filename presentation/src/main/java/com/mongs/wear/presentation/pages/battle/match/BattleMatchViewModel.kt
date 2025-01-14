package com.mongs.wear.presentation.pages.battle.match

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mongs.wear.core.enums.MatchRoundCode
import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.battle.usecase.GetMatchUseCase
import com.mongs.wear.domain.battle.usecase.GetMyMatchPlayerUseCase
import com.mongs.wear.domain.battle.usecase.GetRiverMatchPlayerUseCase
import com.mongs.wear.domain.battle.usecase.ExitMatchUseCase
import com.mongs.wear.domain.battle.usecase.OverMatchUseCase
import com.mongs.wear.domain.battle.usecase.PickMatchUseCase
import com.mongs.wear.domain.battle.usecase.MatchStartUseCase
import com.mongs.wear.domain.battle.vo.MatchPlayerVo
import com.mongs.wear.domain.battle.vo.MatchVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BattleMatchViewModel @Inject constructor(
    private val getMatchUseCase: GetMatchUseCase,
    private val getMyMatchPlayerUseCase: GetMyMatchPlayerUseCase,
    private val getRiverMatchPlayerUseCase: GetRiverMatchPlayerUseCase,
    private val matchStartUseCase: MatchStartUseCase,
    private val pickMatchUseCase: PickMatchUseCase,
    private val overMatchUseCase: OverMatchUseCase,
    private val exitMatchUseCase: ExitMatchUseCase,
): ViewModel() {
    val uiState: UiState = UiState()

    private val _matchVo = MediatorLiveData<MatchVo>()
    val matchVo: LiveData<MatchVo> get() = _matchVo

    private val _myMatchPlayerVo = MediatorLiveData<MatchPlayerVo>()
    val myMatchPlayerVo: LiveData<MatchPlayerVo> get() = _myMatchPlayerVo

    private val _otherMatchPlayerVo = MediatorLiveData<MatchPlayerVo>()
    val otherMatchPlayerVo: LiveData<MatchPlayerVo> get() = _otherMatchPlayerVo

    init {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                _matchVo.addSource(
                    withContext(Dispatchers.IO) {
                        getMatchUseCase()
                    }
                ) { matchVo ->
                    _matchVo.value = matchVo
                }

                _myMatchPlayerVo.addSource(
                    withContext(Dispatchers.IO) {
                        getMyMatchPlayerUseCase()
                    }
                ) { myMatchPlayerVo ->
                    _myMatchPlayerVo.value = myMatchPlayerVo
                }

                _otherMatchPlayerVo.addSource(
                    withContext(Dispatchers.IO) {
                        getRiverMatchPlayerUseCase()
                    }
                ) { otherMatchPlayerVo ->
                    _otherMatchPlayerVo.value = otherMatchPlayerVo
                }

                uiState.isLoading = false

            } catch (_: ErrorException) {
            }
        }
    }

    fun matchStart() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                matchStartUseCase()
            } catch (_: ErrorException) {
            }
        }
    }

    fun matchPick(pickCode: MatchRoundCode) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                pickMatchUseCase(pickCode)
                uiState.matchPickDialog = false
            } catch (_: ErrorException) {
            }
        }
    }

    fun matchOver() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                overMatchUseCase()
                uiState.matchPickDialog = false
            } catch (_: ErrorException) {
            }
        }
    }

    fun matchExit() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
//                exitMatchUseCase()
            } catch (_: ErrorException) {
            }
        }
    }

    class UiState (
        isLoading: Boolean = true,
        matchPickDialog: Boolean = false,
    ) {
        var isLoading by mutableStateOf(isLoading)
        var matchPickDialog by mutableStateOf(matchPickDialog)
    }
}