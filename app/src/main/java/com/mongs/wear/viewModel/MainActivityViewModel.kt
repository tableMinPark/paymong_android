package com.mongs.wear.viewModel

import android.annotation.SuppressLint
import com.mongs.wear.domain.global.client.MqttClient
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
    private val mqttClient: MqttClient,
) : BaseViewModel() {

    fun connectSensor() = CoroutineScope(Dispatchers.IO).launch {
        stepSensorManager.listen()
    }

    fun disconnectSensor() = CoroutineScope(Dispatchers.IO).launch {
        stepSensorManager.stop()
    }

    /**
     * 브로커 일시 중지
     */
//    fun resumeConnectMqtt() = CoroutineScope(Dispatchers.IO).launch {
//        mqttClient.resumeConnect()
//    }

    /**
     * 브로커 재연결
     */
//    fun pauseConnectMqtt() = CoroutineScope(Dispatchers.IO).launch {
//        mqttClient.pauseConnect()
//    }

    fun disconnectMqtt() = CoroutineScope(Dispatchers.IO).launch {
        mqttClient.disconnect()
    }

    override fun exceptionHandler(exception: Throwable) {}
}