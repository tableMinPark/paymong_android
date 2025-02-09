package com.mongs.wear.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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