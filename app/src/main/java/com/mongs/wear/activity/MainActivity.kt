package com.mongs.wear.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mongs.wear.presentation.assets.MongsTheme
import com.mongs.wear.presentation.layout.MainView
import com.mongs.wear.viewModel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * 권한 확인
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
                ActivityCompat.requestPermissions(this, permissions, 100)
            }
        } else {
            if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED
            ) {
                val permissions = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACTIVITY_RECOGNITION,
                )
                ActivityCompat.requestPermissions(this, permissions, 100)
            }
        }

        /**
         * UI 로딩
         */
        setContent {
            MongsTheme {
                MainView()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 네트워크 플래그 초기화
        mainActivityViewModel.initNetwork()
        // 플레이어 정보 동기화
        mainActivityViewModel.updatePlayer()
        // 현재 몽 정보 동기화
        mainActivityViewModel.updateCurrentSlot()
        // 총 걸음 수 서버 동기화
        mainActivityViewModel.updateTotalWalkingCount()
        // 브로커 연결 + 재구독
        mainActivityViewModel.connectMqtt()
    }

    override fun onPause() {
        // 브로커 연결 해제 + 구독 해제
        mainActivityViewModel.pauseMqtt()
        super.onPause()
    }

    override fun onDestroy() {
        // 브로커 연결 해제 + 구독 해제
        mainActivityViewModel.disConnectMqtt()
        super.onDestroy()
    }
}