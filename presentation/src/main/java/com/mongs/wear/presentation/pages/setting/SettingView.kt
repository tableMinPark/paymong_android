package com.mongs.wear.presentation.pages.setting

import android.content.Context
import android.widget.Toast
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
                            Toast.makeText(context, "잠시후 다시 시도", Toast.LENGTH_SHORT).show()
                        }
                },
                cancel = {
                    settingViewModel.uiState.logoutDialog = false
                }
            )
        } else {

            val notification = settingViewModel.notification.observeAsState(false)

            SettingBackground()
            SettingContent(
                notification = notification.value,
                toggleNotification = settingViewModel::toggleNotification,
                logoutDialogOpen = {
                    settingViewModel.uiState.logoutDialog = true
                },
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
    }
}

@Composable
private fun SettingContent(
    notification: Boolean,
    toggleNotification: (Boolean) -> Unit,
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