package com.mongs.wear.presentation.pages.main.walking

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.mongs.wear.core.exception.usecase.ExchangeWalkingCountUseCaseException
import com.mongs.wear.core.exception.usecase.GetCurrentSlotUseCaseException
import com.mongs.wear.core.exception.usecase.GetStepsUseCaseException
import com.mongs.wear.core.utils.TimeUtil
import com.mongs.wear.domain.device.usecase.ExchangeWalkingCountUseCase
import com.mongs.wear.domain.device.usecase.GetStepsUseCase
import com.mongs.wear.domain.management.usecase.GetCurrentSlotUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainWalkingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getCurrentSlotUseCase: GetCurrentSlotUseCase,
    private val getStepsUseCase: GetStepsUseCase,
    private val exchangeWalkingCountUseCase: ExchangeWalkingCountUseCase,
): BaseViewModel() {

    private val _payPoint = MediatorLiveData<Int>()
    val payPoint: LiveData<Int> get() = _payPoint

    private val _steps = MediatorLiveData<Int>()
    val steps: LiveData<Int> get() = _steps

    val activityPermission: LiveData<Boolean> get() = _activityPermission
    private val _activityPermission = MediatorLiveData(false)

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

            _activityPermission.postValue(verifyActivityPermission().isEmpty())

            uiState.loadingBar = false
        }
    }

    /**
     * 활동 권한 부여 여부 갱신
     */
    fun refreshActivityPermission() {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {
            _activityPermission.postValue(verifyActivityPermission().isEmpty())
        }
    }

    /**
     * 걸음 수 환전
     */
    fun exchangeWalkingCount(mongId: Long, walkingCount: Int) {
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

    /**
     * 활동 권한 체크
     */
    private fun verifyActivityPermission() : Array<String> {
        return if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION)
        } else {
            emptyArray()
        }
    }

    val uiState: UiState = UiState()

    class UiState : BaseUiState() {
        var chargePayPointDialog by mutableStateOf(false)
    }

    override suspend fun exceptionHandler(exception: Throwable) {
        when(exception) {
            is GetCurrentSlotUseCaseException -> {
                uiState.loadingBar = false
            }

            is GetStepsUseCaseException -> {
                uiState.loadingBar = false
            }

            is ExchangeWalkingCountUseCaseException -> {
                uiState.loadingBar = false
                uiState.chargePayPointDialog = false
            }

            else -> {
                uiState.loadingBar = false
            }
        }
    }
}