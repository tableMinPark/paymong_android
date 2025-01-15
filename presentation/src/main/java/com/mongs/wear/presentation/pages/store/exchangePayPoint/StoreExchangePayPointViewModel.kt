package com.mongs.wear.presentation.pages.store.exchangePayPoint

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.mongs.wear.domain.device.exception.ExchangeWalkingCountException
import com.mongs.wear.domain.management.exception.GetCurrentSlotException
import com.mongs.wear.domain.management.usecase.GetCurrentSlotUseCase
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.domain.device.exception.GetStepsException
import com.mongs.wear.domain.player.usecase.ExchangeStarPointUseCase
import com.mongs.wear.domain.player.usecase.GetStarPointUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StoreExchangePayPointViewModel @Inject constructor(
    private val getCurrentSlotUseCase: GetCurrentSlotUseCase,
    private val getStarPointUseCase: GetStarPointUseCase,
    private val exchangeStarPointUseCase: ExchangeStarPointUseCase,
): BaseViewModel() {

    private val _mongVo = MediatorLiveData<MongVo?>(null)
    val mongVo: LiveData<MongVo?> get() = _mongVo

    val starPoint: LiveData<Int> get() = _starPoint
    private val _starPoint = MediatorLiveData<Int>()

    init {
        viewModelScopeWithHandler.launch (Dispatchers.Main) {

            uiState.loadingBar = true

            _mongVo.addSource(withContext(Dispatchers.IO) { getCurrentSlotUseCase() }) {
                it?.let { mongVo ->
                    _mongVo.value = mongVo
                }
            }

            _starPoint.addSource(withContext(Dispatchers.IO) { getStarPointUseCase() }) { starPoint ->
                _starPoint.value = starPoint
            }

            uiState.loadingBar = false
        }
    }

    fun exchangeStarPoint(mongId: Long, starPoint: Int) {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {

            uiState.loadingBar = true
            uiState.exchangeStarPointDialog = false

            exchangeStarPointUseCase(
                ExchangeStarPointUseCase.Param(
                    mongId = mongId,
                    starPoint = starPoint,
                )
            )

            toastEvent("환전 성공")

            uiState.loadingBar = false
        }
    }

    val uiState: UiState = UiState()

    class UiState : BaseUiState() {
        var exchangeStarPointDialog by mutableStateOf(false)
    }

    override fun exceptionHandler(exception: Throwable) {

        when(exception) {
            is GetStepsException -> {
                uiState.loadingBar = false
            }

            is GetCurrentSlotException -> {
                uiState.loadingBar = false
            }

            is ExchangeWalkingCountException -> {
                uiState.loadingBar = false
                uiState.exchangeStarPointDialog = false
            }

            else -> {
                uiState.loadingBar = false
            }
        }
    }
}