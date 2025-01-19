package com.mongs.wear.presentation.layout

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mongs.wear.domain.device.usecase.GetNetworkUseCase
import com.mongs.wear.domain.device.usecase.SetDeviceIdUseCase
import com.mongs.wear.domain.device.usecase.SetNetworkUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import com.mongs.wear.presentation.global.worker.StepSensorWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("HardwareIds")
@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val setNetworkUseCase: SetNetworkUseCase,
    private val getNetworkUseCase: GetNetworkUseCase,
    private val setDeviceIdUseCase: SetDeviceIdUseCase,
    private val workerManager: WorkManager,
) : BaseViewModel() {

    val network: LiveData<Boolean> get() = _network
    private val _network = MediatorLiveData(true)

    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {

            uiState.loadingBar = true

            // 기기 ID 설정
            setDeviceIdUseCase(
                SetDeviceIdUseCase.Param(
                    deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID),
                )
            )

            // network flag true 로 변경
            setNetworkUseCase(
                SetNetworkUseCase.Param(
                    network = true
                )
            )

            // network flag 옵저버 객체 조회
            _network.addSource(withContext(Dispatchers.IO) { getNetworkUseCase() }) {
                _network.value = it
            }

            // 15분 간격 걸음 수 서버 동기화 워커 실행
            workerManager.enqueueUniquePeriodicWork(
                StepSensorWorker.WORKER_NAME,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                PeriodicWorkRequestBuilder<StepSensorWorker>(15, TimeUnit.MINUTES).build()
            )

            uiState.loadingBar = false
        }
    }

    val uiState = UiState()

    class UiState : BaseUiState() {}

    override fun exceptionHandler(exception: Throwable) {

        when (exception) {
            else -> {
                uiState.loadingBar = false
            }
        }
    }
}