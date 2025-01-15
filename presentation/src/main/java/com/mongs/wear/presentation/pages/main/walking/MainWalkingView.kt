package com.mongs.wear.presentation.pages.main.walking

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.wear.compose.material.Text
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.component.common.bar.LoadingBar
import com.mongs.wear.presentation.component.common.button.BlueButton
import com.mongs.wear.presentation.component.common.button.LeftButton
import com.mongs.wear.presentation.component.common.button.RightButton
import com.mongs.wear.presentation.component.common.textbox.PayPoint
import com.mongs.wear.presentation.dialog.common.ConfirmAndCancelDialog
import com.mongs.wear.presentation.pages.main.walking.MainWalkingViewModel.UiState
import kotlin.math.max
import kotlin.math.min

@Composable
fun MainWalkingView(
    mongVo: MongVo,
    mainWalkingViewModel: MainWalkingViewModel = hiltViewModel(),
) {

    val payPoint = mainWalkingViewModel.payPoint.observeAsState(0)
    val ratio = remember { mutableIntStateOf(0) }
    val steps = mainWalkingViewModel.steps.observeAsState(0)
    val remainingSteps = remember { derivedStateOf { steps.value - 1000 * ratio.intValue }}
    val chargePayPoint = remember { derivedStateOf { 100 * ratio.intValue }}

    Box {
        if (mainWalkingViewModel.uiState.loadingBar) {
            MainWalkingLoadingBar()
        } else if(mainWalkingViewModel.uiState.chargePayPointDialog) {
            ConfirmAndCancelDialog(
                text = "$${chargePayPoint.value}\n환전하시겠습니까?",
                confirm = {
                    mainWalkingViewModel.chargePayPoint(
                        mongId = mongVo.mongId,
                        walkingCount = 1000 * ratio.intValue,
                    )
                    ratio.intValue = 0
                },
                cancel = { mainWalkingViewModel.uiState.chargePayPointDialog = false }
            )
        } else {
            MainWalkingContent(
                mongVo = mongVo,
                chargePayPoint = chargePayPoint.value,
                payPoint = payPoint.value,
                remainingSteps = remainingSteps.value,
                increaseRatio = { ratio.intValue = min(ratio.intValue + 1, steps.value / 1000) },
                decreaseRatio = { ratio.intValue = max(ratio.intValue - 1, 0) },
                modifier = Modifier.zIndex(1f),
                uiState = mainWalkingViewModel.uiState,
            )
        }
    }
}

@Composable
private fun MainWalkingContent(
    mongVo: MongVo?,
    chargePayPoint: Int,
    payPoint: Int,
    remainingSteps: Int,
    increaseRatio: () -> Unit,
    decreaseRatio: () -> Unit,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {},
                        ),
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f)
                    ) {
                        Text(
                            text = "$remainingSteps 걸음",
                            textAlign = TextAlign.Center,
                            fontFamily = DAL_MU_RI,
                            fontWeight = FontWeight.Light,
                            fontSize = 18.sp,
                            color = MongsWhite,
                            maxLines = 1,
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f)
                    ) {
                        LeftButton(onClick = decreaseRatio)

                        Spacer(modifier = Modifier.width(30.dp))

                        Image(
                            painter = painterResource(R.drawable.pointlogo),
                            contentDescription = null,
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp),
                            contentScale = ContentScale.FillBounds,
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "+ $chargePayPoint",
                            textAlign = TextAlign.Center,
                            fontFamily = DAL_MU_RI,
                            fontWeight = FontWeight.Light,
                            fontSize = 18.sp,
                            color = MongsWhite,
                            maxLines = 1,
                        )

                        Spacer(modifier = Modifier.width(30.dp))

                        RightButton(onClick = increaseRatio)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
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
private fun MainWalkingLoadingBar(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        LoadingBar()
    }
}