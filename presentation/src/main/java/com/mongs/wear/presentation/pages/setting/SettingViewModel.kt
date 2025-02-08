package com.mongs.wear.presentation.pages.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.work.WorkManager
import com.mongs.wear.core.exception.usecase.LogoutUseCaseException
import com.mongs.wear.domain.auth.usecase.LogoutUseCase
import com.mongs.wear.domain.device.usecase.GetNotificationUseCase
import com.mongs.wear.domain.device.usecase.SetNotificationUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import com.mongs.wear.presentation.global.worker.StepSensorWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val workerManager: WorkManager,
    private val logoutUseCase: LogoutUseCase,
    private val getNotificationUseCase: GetNotificationUseCase,
    private val setNotificationUseCase: SetNotificationUseCase,
) : BaseViewModel() {

    val notification: LiveData<Boolean> get() = _notification
    private val _notification = MediatorLiveData(false)


    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {

            uiState.loadingBar = true

            // notification flag 옵저버 객체 조회
            _notification.addSource(withContext(Dispatchers.IO) { getNotificationUseCase() }) {
                _notification.value = it
            }

            uiState.loadingBar = false
        }
    }

    /**
     * 로그 아웃
     */
    fun logout() {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {
            logoutUseCase()

            scrollPageMainPagerView()

            // 워커 삭제
            workerManager.cancelUniqueWork(StepSensorWorker.WORKER_NAME)

            uiState.navLoginView = true
        }
    }

    /**
     * 알림 여부 토글
     */
    fun toggleNotification(notification: Boolean) {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {
            setNotificationUseCase(
                SetNotificationUseCase.Param(
                    notification = notification,
                )
            )
        }
    }

    val uiState = UiState()

    class UiState : BaseUiState() {
        var navLoginView by mutableStateOf(false)
        var logoutDialog by mutableStateOf(false)
    }

    override suspend fun exceptionHandler(exception: Throwable) {
        when(exception) {
            is LogoutUseCaseException -> {
                uiState.loadingBar = false
                uiState.logoutDialog = false
            }

            else -> {
                uiState.loadingBar = false
            }
        }
    }
}