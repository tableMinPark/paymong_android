package com.mongs.wear.presentation.pages.setting

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val workerManager: WorkManager,
    private val logoutUseCase: LogoutUseCase,
    private val getNotificationUseCase: GetNotificationUseCase,
    private val setNotificationUseCase: SetNotificationUseCase,
) : BaseViewModel() {

    val notification: LiveData<Boolean> get() = _notification
    private val _notification = MediatorLiveData(false)

    val notificationPermission: LiveData<Boolean> get() = _notificationPermission
    private val _notificationPermission = MediatorLiveData(false)

    val activityPermission: LiveData<Boolean> get() = _activityPermission
    private val _activityPermission = MediatorLiveData(false)

    val locationPermission: LiveData<Boolean> get() = _locationPermission
    private val _locationPermission = MediatorLiveData(false)

    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {

            uiState.loadingBar = true

            // notification flag 옵저버 객체 조회
            _notification.addSource(withContext(Dispatchers.IO) { getNotificationUseCase() }) {
                _notification.value = it
            }

            _notificationPermission.postValue(verifyNotificationPermission().isEmpty())

            _activityPermission.postValue(verifyActivityPermission().isEmpty())

            _locationPermission.postValue(verifyLocationPermission().isEmpty())

            uiState.loadingBar = false
        }
    }

    /**
     * 권한 부여 여부 확인
     */
    fun checkPermission() {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {

            _notificationPermission.postValue(verifyNotificationPermission().isEmpty())

            _activityPermission.postValue(verifyActivityPermission().isEmpty())

            _locationPermission.postValue(verifyLocationPermission().isEmpty())

            uiState.loadingBar = false
        }
    }

    /**
     * 알림 권한 부여 요청
     */
    fun requestNotificationPermission() {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {
            uiState.loadingBar = true
            uiState.requestPermissionEvent.emit(verifyNotificationPermission())
        }
    }

    /**
     * 활동 권한 부여 요청
     */
    fun requestActivityPermission() {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {
            uiState.loadingBar = true
            uiState.requestPermissionEvent.emit(verifyActivityPermission())
        }
    }

    /**
     * 백그라운드 위치 권한 부여 요청
     */
    fun requestLocationBackgroundPermission() {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {
            uiState.loadingBar = true
            uiState.requestPermissionEvent.emit(verifyLocationPermission())
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


    /**
     * 알림 권한 체크
     */
    private fun verifyNotificationPermission() : Array<String> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                return arrayOf(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        return emptyArray()
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

    /**
     * 위치 권한 체크
     */
    private fun verifyLocationPermission() : Array<String> {
        val permissions = ArrayList<String>()

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        return permissions.toTypedArray()
    }

    val uiState = UiState()

    class UiState : BaseUiState() {
        var requestPermissionEvent = MutableSharedFlow<Array<String>>()
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