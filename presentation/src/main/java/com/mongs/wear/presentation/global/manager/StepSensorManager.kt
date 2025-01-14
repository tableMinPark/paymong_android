package com.mongs.wear.presentation.global.manager

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import android.util.Log
import com.mongs.wear.domain.player.usecase.SetTotalWalkingCountUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class StepSensorManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val setTotalWalkingCountUseCase: SetTotalWalkingCountUseCase
) {

    companion object {
        private const val TAG = "StepSensorManager"
    }

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
    //TYPE_STEP_DETECTOR TYPE_STEP_COUNTER TYPE_GRAVITY

    /**
     * 지속적 걸음 수 센서 값 조회
     * DataStore 상 totalWalkingCount 동기화
     */
    private val stepListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                CoroutineScope(Dispatchers.IO).launch {
//                    val totalWalkingCount = event.values[0].toInt()
                    val totalWalkingCount = SystemClock.uptimeMillis().toInt()      // TODO: 센서 값 변경

                    Log.i(TAG, "[Manager] 총 걸음 수 : $totalWalkingCount")

                    setTotalWalkingCountUseCase(
                        SetTotalWalkingCountUseCase.Param(
                            totalWalkingCount = totalWalkingCount
                        )
                    )
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    fun listen() {
        stepSensor?.let {
            sensorManager.registerListener(stepListener, it, SensorManager.SENSOR_DELAY_FASTEST)
            Log.i(TAG, "[Manager] 스텝 센서 수신 시작")
        }
    }

    fun stop() {
        sensorManager.unregisterListener(stepListener)
        Log.i(TAG, "[Manager] 스텝 센서 수신 중지")
    }
}