package com.mongs.wear.presentation.layout

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mongs.wear.core.exception.usecase.ConnectMqttUseCaseException
import com.mongs.wear.domain.battle.usecase.UpdateMatchUseCase
import com.mongs.wear.domain.device.usecase.GetNetworkUseCase
import com.mongs.wear.domain.device.usecase.SetDeviceIdUseCase
import com.mongs.wear.domain.global.usecase.ConnectMqttUseCase
import com.mongs.wear.domain.management.usecase.UpdateCurrentSlotUseCase
import com.mongs.wear.domain.player.usecase.UpdatePlayerUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import com.mongs.wear.presentation.global.worker.LocationSensorWorker
import com.mongs.wear.presentation.global.worker.StepSensorWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("HardwareIds")
@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val workerManager: WorkManager,
    private val setDeviceIdUseCase: SetDeviceIdUseCase,
    private val getNetworkUseCase: GetNetworkUseCase,
    private val connectMqttUseCase: ConnectMqttUseCase,
    private val updateCurrentSlotUseCase: UpdateCurrentSlotUseCase,
    private val updatePlayerUseCase: UpdatePlayerUseCase,
    private val updateMatchUseCase: UpdateMatchUseCase,
) : BaseViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    val network: LiveData<Boolean> get() = _network
    private val _network = MediatorLiveData(true)

    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {
            /**
             * 필수 권한 확인 로직 수행
             */
            uiState.permissionLoadingBar = true

            /**
             * 백그라운드 위치 권한 확인
             * TODO: 삭제
              */
//            if (verifyBackgroundLocationPermission().isNotEmpty()) {
//                toastEvent("위치 권한 필요")
//            }

            val permissions = verifyNotificationPermission() + verifyActivityPermission() + verifyLocationPermission()

            if (permissions.isNotEmpty()) {
                uiState.requestPermissionEvent.emit(permissions.toTypedArray())
            } else {
                uiState.permissionLoadingBar = false
            }

            /**
             * 초기 로직 수행
             */
            uiState.loadingBar = true

            // network flag 옵저버 객체 조회
            _network.addSource(withContext(Dispatchers.IO) { getNetworkUseCase() }) {
                _network.value = it
            }

            // 기기 ID 설정
            setDeviceIdUseCase(
                SetDeviceIdUseCase.Param(
                    deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID),
                )
            )

            // 15분 간격 걸음 수 서버 동기화 워커 실행
            workerManager.enqueueUniquePeriodicWork(
                StepSensorWorker.WORKER_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                PeriodicWorkRequestBuilder<StepSensorWorker>(15, TimeUnit.MINUTES).build()
            )

            // 15분 간격 GPS 수신 워커 실행
            workerManager.enqueueUniquePeriodicWork(
                LocationSensorWorker.WORKER_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                PeriodicWorkRequestBuilder<LocationSensorWorker>(15, TimeUnit.MINUTES).build()
            )

            uiState.loadingBar = false
        }
    }

    /**
     * 권한 부여 여부 확인
     */
    fun checkPermission() {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {
            uiState.permissionLoadingBar = false
        }
    }

    /**
     * 네트워크 연결 재시도
     */
    fun retryNetwork() {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {

            uiState.errorDialogLoadingBar = true

            delay(700)

            connectMqttUseCase()

            updatePlayerUseCase()

            updateCurrentSlotUseCase()

            updateMatchUseCase()

            uiState.errorDialogLoadingBar = false
        }
    }

    /**
     * 알림 권한 체크
     */
    private fun verifyNotificationPermission() : ArrayList<String> {
        val permissions = ArrayList<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        return permissions
    }

    /**
     * 활동 권한 체크
     */
    private fun verifyActivityPermission() : ArrayList<String> {
        val permissions = ArrayList<String>()

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            permissions.add(Manifest.permission.ACTIVITY_RECOGNITION)
        }

        return permissions
    }

    /**
     * 위치 권한 체크
     */
    private fun verifyLocationPermission() : ArrayList<String> {
        val permissions = ArrayList<String>()

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        return permissions
    }


    /**
     * 백그라운드 위치 권한 체크
     */
    private fun verifyBackgroundLocationPermission() : ArrayList<String> {
        val permissions = ArrayList<String>()

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_DENIED) {
            permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        return permissions
    }

    val uiState = UiState()

    class UiState : BaseUiState() {
        var requestPermissionEvent = MutableSharedFlow<Array<String>>()
        var permissionLoadingBar by mutableStateOf(false)
        var errorDialogLoadingBar by mutableStateOf(false)
    }

    override suspend fun exceptionHandler(exception: Throwable) {
        exception.printStackTrace()
        when (exception) {
            is ConnectMqttUseCaseException -> {
                uiState.errorDialogLoadingBar = false
            }

            else -> {
                uiState.loadingBar = false
                uiState.permissionLoadingBar = false
            }
        }
    }
}