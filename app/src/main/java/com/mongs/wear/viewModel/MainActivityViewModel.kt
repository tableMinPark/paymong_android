package com.mongs.wear.viewModel

import android.annotation.SuppressLint
import com.mongs.wear.domain.device.usecase.ConnectMqttUseCase
import com.mongs.wear.domain.device.usecase.DisConnectMqttUseCase
import com.mongs.wear.domain.device.usecase.SetNetworkUseCase
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.domain.management.usecase.UpdateCurrentSlotUseCase
import com.mongs.wear.domain.device.usecase.SetServerTotalWalkingCountUseCase
import com.mongs.wear.domain.player.usecase.UpdatePlayerUseCase
import com.mongs.wear.presentation.global.manager.StepSensorManager
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("HardwareIds")
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val stepSensorManager: StepSensorManager,
    private val setNetworkUseCase: SetNetworkUseCase,
    private val updateCurrentSlotUseCase: UpdateCurrentSlotUseCase,
    private val updatePlayerUseCase: UpdatePlayerUseCase,
    private val setServerTotalWalkingCountUseCase: SetServerTotalWalkingCountUseCase,
    private val connectMqttUseCase: ConnectMqttUseCase,
    private val disConnectMqttUseCase: DisConnectMqttUseCase,
) : BaseViewModel() {

    /**
     * 네트워크 플래그 true 변경
     */
    fun initNetwork() {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {
            setNetworkUseCase(
                SetNetworkUseCase.Param(
                    network = true
                )
            )
        }
    }

    /**
     * 플레이어 정보 동기화
     */
    fun updatePlayer() {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {
            updatePlayerUseCase()
        }
    }

    /**
     * 현재 몽 동기화
     */
    fun updateCurrentMong() {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {
            updateCurrentSlotUseCase()
        }
    }

    /**
     * 걸음 수 동기화
     */
    fun updateTotalWalkingCount() {
        // 로그인 여부 확인
        viewModelScopeWithHandler.launch(Dispatchers.IO) {

            val totalWalkingCount = stepSensorManager.getWalkingCount()

            setServerTotalWalkingCountUseCase(
                SetServerTotalWalkingCountUseCase.Param(
                    totalWalkingCount = totalWalkingCount
                )
            )
        }
    }

    /**
     * 브로커 일시 중지
     */
    fun connectMqtt() = CoroutineScope(Dispatchers.IO).launch {
//         mqttClient.resumeConnect()
        connectMqttUseCase()
    }

    /**
     * 브로커 재연결
     */
    fun disConnectMqtt() = CoroutineScope(Dispatchers.IO).launch {
//         mqttClient.pauseConnect()
        disConnectMqttUseCase()
    }

    val uiState = UiState()

    class UiState : BaseUiState()

    override fun exceptionHandler(exception: Throwable) {}
}