package com.mongs.wear.presentation.pages.main.walking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.mongs.wear.core.utils.TimeUtil
import com.mongs.wear.domain.device.exception.ExchangeWalkingCountException
import com.mongs.wear.domain.device.usecase.ExchangeWalkingCountUseCase
import com.mongs.wear.domain.management.exception.GetCurrentSlotException
import com.mongs.wear.domain.management.usecase.GetCurrentSlotUseCase
import com.mongs.wear.domain.device.exception.GetStepsException
import com.mongs.wear.domain.device.usecase.GetStepsUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainWalkingViewModel @Inject constructor(
    private val getCurrentSlotUseCase: GetCurrentSlotUseCase,
    private val getStepsUseCase: GetStepsUseCase,
    private val exchangeWalkingCountUseCase: ExchangeWalkingCountUseCase,
): BaseViewModel() {

    private val _payPoint = MediatorLiveData<Int>()
    val payPoint: LiveData<Int> get() = _payPoint

    private val _steps = MediatorLiveData<Int>()
    val steps: LiveData<Int> get() = _steps

    init {
        viewModelScopeWithHandler.launch (Dispatchers.Main) {

            uiState.loadingBar = true

            _payPoint.addSource(withContext(Dispatchers.IO) { getCurrentSlotUseCase() }) {
                it?.let { mongVo ->
                    _payPoint.value = mongVo.payPoint
                } ?: run { _payPoint.value = 0 }
            }

            _steps.addSource(withContext(Dispatchers.IO) { getStepsUseCase() }) { steps ->
                _steps.value = steps
            }

            uiState.loadingBar = false
        }
    }

    fun chargePayPoint(mongId: Long, walkingCount: Int) {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {

            uiState.loadingBar = true
            uiState.chargePayPointDialog = false

            val deviceBootedDt = TimeUtil.getBootedDt()

            exchangeWalkingCountUseCase(
                ExchangeWalkingCountUseCase.Param(
                    mongId = mongId,
                    walkingCount = walkingCount,
                    deviceBootedDt = deviceBootedDt,
                )
            )

            toastEvent("환전 성공")

            uiState.loadingBar = false
        }
    }

    val uiState: UiState = UiState()

    class UiState : BaseUiState() {
        var chargePayPointDialog by mutableStateOf(false)
    }

    override fun exceptionHandler(exception: Throwable) {

        when(exception) {
            is GetCurrentSlotException -> {
                uiState.loadingBar = false
            }

            is GetStepsException -> {
                uiState.loadingBar = false
            }

            is ExchangeWalkingCountException -> {
                uiState.loadingBar = false
                uiState.chargePayPointDialog = false
            }

            else -> {
                uiState.loadingBar = false
            }
        }
    }
}