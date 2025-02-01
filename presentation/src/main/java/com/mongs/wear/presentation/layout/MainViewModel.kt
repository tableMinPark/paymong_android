package com.mongs.wear.presentation.layout

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mongs.wear.domain.device.exception.ConnectMqttException
import com.mongs.wear.domain.device.usecase.ConnectMqttUseCase
import com.mongs.wear.domain.device.usecase.GetNetworkUseCase
import com.mongs.wear.domain.device.usecase.SetDeviceIdUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import com.mongs.wear.presentation.global.worker.StepSensorWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
) : BaseViewModel() {

    val network: LiveData<Boolean> get() = _network
    private val _network = MediatorLiveData(true)

    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {

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

            uiState.loadingBar = false
        }
    }

    /**
     * 네트워크 연결 재시도
     */
    fun networkRetry() {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {

            uiState.errorDialogLoadingBar = true

            delay(1000)

            connectMqttUseCase()

            uiState.errorDialogLoadingBar = false
        }
    }


    val uiState = UiState()

    class UiState : BaseUiState() {
        var errorDialogLoadingBar by mutableStateOf(false)
    }

    override fun exceptionHandler(exception: Throwable) {

        when (exception) {

            is ConnectMqttException -> {
                uiState.errorDialogLoadingBar = false
            }

            else -> {
                uiState.loadingBar = false
            }
        }
    }
}