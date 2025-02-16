package com.mongs.wear.presentation.pages.exchange.walking

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.component.background.ExchangeWalkingBackground
import com.mongs.wear.presentation.component.common.bar.LoadingBar
import com.mongs.wear.presentation.component.common.button.BlueButton
import com.mongs.wear.presentation.component.common.button.SelectButton
import com.mongs.wear.presentation.component.common.textbox.PayPoint
import com.mongs.wear.presentation.dialog.common.ConfirmAndCancelDialog
import com.mongs.wear.presentation.dialog.error.NeedPermissionDialog
import com.mongs.wear.presentation.global.view.OnLeavePage
import com.mongs.wear.presentation.pages.exchange.walking.ExchangeWalkingViewModel.UiState
import kotlin.math.max
import kotlin.math.min

@Composable
fun ExchangeWalkingView(
    navController: NavController,
    exchangeWalkingViewModel: ExchangeWalkingViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val mongVo = exchangeWalkingViewModel.mongVo.observeAsState()
    val payPoint = exchangeWalkingViewModel.payPoint.observeAsState(0)
    val ratio = remember { mutableIntStateOf(0) }
    val steps = exchangeWalkingViewModel.steps.observeAsState(0)
    val remainingSteps = remember { derivedStateOf { steps.value - 1000 * ratio.intValue }}
    val chargePayPoint = remember { derivedStateOf { 100 * ratio.intValue }}
    val activityPermission = exchangeWalkingViewModel.activityPermission.observeAsState(true)

    Box {
        ExchangeWalkingBackground()

        if (exchangeWalkingViewModel.uiState.loadingBar) {
            ExchangeWalkingLoadingBar()
        } else {
            mongVo.value?.let { mongVo ->
                if (exchangeWalkingViewModel.uiState.chargePayPointDialog) {
                    ConfirmAndCancelDialog(
                        text = "$${chargePayPoint.value}\n환전하시겠습니까?",
                        confirm = {
                            exchangeWalkingViewModel.exchangeWalkingCount(
                                mongId = mongVo.mongId,
                                walkingCount = 1000 * ratio.intValue,
                            )
                            ratio.intValue = 0
                        },
                        cancel = { exchangeWalkingViewModel.uiState.chargePayPointDialog = false }
                    )
                } else {
                    ExchangeWalkingContent(
                        mongVo = mongVo,
                        ratio = ratio.intValue,
                        steps = steps.value,
                        decreaseRatio = { ratio.intValue = max(ratio.intValue - 1, 0) },
                        increaseRatio = {
                            ratio.intValue = min(ratio.intValue + 1, steps.value / 1000)
                        },
                        chargePayPoint = chargePayPoint.value,
                        payPoint = payPoint.value,
                        remainingSteps = remainingSteps.value,
                        modifier = Modifier.zIndex(1f),
                        uiState = exchangeWalkingViewModel.uiState,
                    )

                    if (!activityPermission.value) {
                        NeedPermissionDialog(
                            permissionName = "활동",
                            permissionSettingEndEvent = exchangeWalkingViewModel::refreshActivityPermission,
                            modifier = Modifier.zIndex(2f)
                        )
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            exchangeWalkingViewModel.connectSensor()
        }

        OnLeavePage(
            navController = navController,
            lifecycleOwner = lifecycleOwner,
            onLeavePage = exchangeWalkingViewModel::disconnectSensor
        )
    }
}

@Composable
private fun ExchangeWalkingContent(
    mongVo: MongVo?,
    ratio: Int,
    steps: Int,
    decreaseRatio: (Int) -> Unit,
    increaseRatio: (Int) -> Unit,
    chargePayPoint: Int,
    payPoint: Int,
    remainingSteps: Int,
    modifier: Modifier = Modifier.zIndex(0f),
    uiState: UiState,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(15.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
            ) {
                PayPoint(payPoint = payPoint)
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
            ) {
                Text(
                    text = "$remainingSteps 걸음",
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                    color = MongsWhite,
                    maxLines = 1,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.35f)
            ) {
                SelectButton(
                    leftBtnDisabled = ratio == 0,
                    rightBtnDisabled = ratio >= steps / 1000,
                    leftBtnClick = { decreaseRatio(max(ratio - 1, 0)) },
                    rightBtnClick = { increaseRatio(min(ratio + 1, steps / 1000)) },
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.point_icon_pay),
                            contentDescription = null,
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp),
                            contentScale = ContentScale.FillBounds,
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "+ $chargePayPoint",
                            textAlign = TextAlign.Center,
                            fontFamily = DAL_MU_RI,
                            fontWeight = FontWeight.Light,
                            fontSize = 18.sp,
                            color = MongsWhite,
                            maxLines = 1,
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f)
            ) {
                BlueButton(
                    text = "환전",
                    width = 70,
                    disable = chargePayPoint == 0 || mongVo == null,
                    onClick = { uiState.chargePayPointDialog = true },
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
private fun ExchangeWalkingLoadingBar(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        LoadingBar()
    }
}