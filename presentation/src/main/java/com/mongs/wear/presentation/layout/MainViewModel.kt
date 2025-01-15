package com.mongs.wear.presentation.layout

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessaging
import com.mongs.wear.core.utils.TimeUtil
import com.mongs.wear.domain.device.usecase.CreateDeviceUseCase
import com.mongs.wear.domain.device.usecase.GetNetworkUseCase
import com.mongs.wear.domain.device.usecase.SetNetworkUseCase
import com.mongs.wear.presentation.global.manager.StepSensorManager
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import com.mongs.wear.presentation.global.worker.StepSensorWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@SuppressLint("HardwareIds")
@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val setNetworkUseCase: SetNetworkUseCase,
    private val getNetworkUseCase: GetNetworkUseCase,
    private val createDeviceUseCase: CreateDeviceUseCase,
    private val stepSensorManager: StepSensorManager,
    private val workerManager: WorkManager,
    private val firebaseMessaging: FirebaseMessaging,
) : BaseViewModel() {

    val network: LiveData<Boolean> get() = _network
    private val _network = MediatorLiveData(true)

    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {

            uiState.loadingBar = true

            // network flag true 로 변경
            setNetworkUseCase(param =
                SetNetworkUseCase.Param(
                    network = true
                )
            )

            // network flag 옵저버 객체 조회
            _network.addSource(withContext(Dispatchers.IO) { getNetworkUseCase() }) {
                _network.value = it
            }

            // 기기 정보 등록 + 걸음 수 갱신
            createDeviceUseCase(
                CreateDeviceUseCase.Param(
                    deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID),
                    totalWalkingCount = stepSensorManager.getWalkingCount(),
                    deviceBootedDt = TimeUtil.getBootedDt(),
                    fcmToken = firebaseMessaging.getTokenSuspend(),
                )
            )

            // 15분 간격 걸음 수 서버 동기화 워커 실행
            workerManager.enqueueUniquePeriodicWork(
                StepSensorWorker.WORKER_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<StepSensorWorker>(15, TimeUnit.MINUTES).build()
            )

            uiState.loadingBar = false
        }
    }

    /**
     * FCM 토큰 조회
     */
    private suspend fun FirebaseMessaging.getTokenSuspend(): String = suspendCancellableCoroutine { cont ->
        getToken().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                cont.resume(task.result ?: "")
            } else {
                cont.resumeWithException(task.exception ?: Exception("Unknown error occurred"))
            }
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