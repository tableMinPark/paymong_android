package com.mongs.wear.presentation.pages.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Text
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.mongs.wear.core.errors.PresentationErrorCode
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.assets.NavItem
import com.mongs.wear.presentation.component.common.background.SettingBackground
import com.mongs.wear.presentation.component.common.bar.LoadingBar
import com.mongs.wear.presentation.component.common.chip.Chip
import com.mongs.wear.presentation.component.common.chip.ToggleChip
import com.mongs.wear.presentation.dialog.common.ConfirmAndCancelDialog

@Composable
fun SettingView(
    navController: NavController,
    settingViewModel: SettingViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
) {

    Box {
        if (settingViewModel.uiState.loadingBar) {
            SettingBackground()
            SettingLoadingBar()
        } else if (settingViewModel.uiState.logoutDialog) {
            ConfirmAndCancelDialog(
                text = "로그아웃하시겠습니까?",
                confirm = {
                    settingViewModel.uiState.loadingBar = true
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .build()
                    val client = GoogleSignIn.getClient(context, gso)
                    client.signOut()
                        .addOnCompleteListener {
                            settingViewModel.logout()
                        }.addOnFailureListener {
                            Toast.makeText(
                                context,
                                PresentationErrorCode.PRESENTATION_AUTH_LOGOUT.getMessage(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                },
                cancel = {
                    settingViewModel.uiState.logoutDialog = false
                }
            )
        } else {

            val notification = settingViewModel.notification.observeAsState(false)

            val notificationPermission = settingViewModel.notificationPermission.observeAsState(false)
            val activityPermission = settingViewModel.activityPermission.observeAsState(false)
            val locationBackgroundPermission = settingViewModel.locationBackgroundPermission.observeAsState(false)

            SettingBackground()
            SettingContent(
                notification = notification.value,
                notificationPermission = notificationPermission.value,
                activityPermission = activityPermission.value,
                locationBackgroundPermission = locationBackgroundPermission.value,
                toggleNotification = settingViewModel::toggleNotification,
                requestNotificationPermission = settingViewModel::requestNotificationPermission,
                requestActivityPermission = settingViewModel::requestActivityPermission,
                requestLocationBackgroundPermission = settingViewModel::requestLocationBackgroundPermission,
                logoutDialogOpen = { settingViewModel.uiState.logoutDialog = true },
                modifier = Modifier.zIndex(1f),
            )
        }

        LaunchedEffect(settingViewModel.uiState.navLoginView) {
            if (settingViewModel.uiState.navLoginView) {
                navController.navigate(NavItem.Login.route) {
                    popUpTo(navController.graph.id)
                }
            }
        }

        /**
         * 권한 요청 설정 화면 이동
         */
        val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            settingViewModel.checkPermission()
        }

        LaunchedEffect(settingViewModel.uiState.requestPermissionEvent) {
            settingViewModel.uiState.requestPermissionEvent.collect { _ ->
                permissionLauncher.launch(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingContent(
    notification: Boolean,
    notificationPermission: Boolean,
    activityPermission: Boolean,
    locationBackgroundPermission: Boolean,
    toggleNotification: (Boolean) -> Unit,
    requestNotificationPermission: () -> Unit,
    requestActivityPermission: () -> Unit,
    requestLocationBackgroundPermission: () -> Unit,
    logoutDialogOpen: () -> Unit,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    val listState = rememberScalingLazyListState(initialCenterItemIndex = 1)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        PositionIndicator(scalingLazyListState = listState)
        ScalingLazyColumn(
            contentPadding = PaddingValues(vertical = 60.dp, horizontal = 6.dp),
            modifier = Modifier.fillMaxSize(),
            state = listState,
            autoCentering = null,
        ) {
            /**
             * 앱 설정
             */
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    Text(
                        text = "설정",
                        textAlign = TextAlign.Center,
                        fontFamily = DAL_MU_RI,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        color = MongsWhite,
                        maxLines = 1,
                    )
                }
            }

            item {
                ToggleChip(
                    fontColor = Color.White,
                    backgroundColor = Color.Black,
                    label = "알림",
                    checked = notification,
                    disabled = !notificationPermission,
                    onChanged = { notification ->
                        toggleNotification(notification)
                    },
                )
            }

            item {
                Chip(
                    fontColor = Color.White,
                    backgroundColor = Color.Black,
                    label = "로그아웃",
                    secondaryLabel = "구글 계정 로그아웃",
                    onClick = logoutDialogOpen,
                )
            }

            /**
             * 권한 설정
             */
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    Text(
                        text = "권한",
                        textAlign = TextAlign.Center,
                        fontFamily = DAL_MU_RI,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        color = MongsWhite,
                        maxLines = 1,
                    )
                }
            }

            item {
                ToggleChip(
                    fontColor = Color.White,
                    backgroundColor = Color.Black,
                    label = "알림",
                    checked = notificationPermission,
                    disabled = false,
                    onChanged = { _ ->
                        requestNotificationPermission()
                    },
                )
            }

            item {
                ToggleChip(
                    fontColor = Color.White,
                    backgroundColor = Color.Black,
                    label = "활동",
                    checked = activityPermission,
                    disabled = false,
                    onChanged = { _ ->
                        requestActivityPermission()
                    },
                )
            }

            item {
                ToggleChip(
                    fontColor = Color.White,
                    backgroundColor = Color.Black,
                    label = "백그라운드 위치",
                    checked = locationBackgroundPermission,
                    disabled = false,
                    onChanged = { _ ->
                        requestLocationBackgroundPermission()
                    },
                )
            }
        }
    }
}

@Composable
private fun SettingLoadingBar(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        LoadingBar()
    }
}