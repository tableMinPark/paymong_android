package com.mongs.wear.presentation.pages.training.runner

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mongs.wear.core.enums.TrainingCode
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.MongResourceCode
import com.mongs.wear.presentation.assets.NavItem
import com.mongs.wear.presentation.component.background.TrainingNestedBackground
import com.mongs.wear.presentation.component.common.bar.LoadingBar
import com.mongs.wear.presentation.component.common.textbox.PayPoint
import com.mongs.wear.presentation.dialog.training.TrainingEndDialog
import com.mongs.wear.presentation.dialog.training.TrainingStartDialog

@Composable
fun TrainingRunnerView(
    navController: NavController,
    trainingRunnerViewModel: TrainingRunnerViewModel = hiltViewModel(),
) {
    Box {
        if (trainingRunnerViewModel.uiState.loadingBar) {
            TrainingNestedBackground()
            TrainingRunnerLoadingBar()
        } else {

            val runnerPayPoint = trainingRunnerViewModel.trainingPayPoint.observeAsState(0)
            val mongVo = trainingRunnerViewModel.mongVo.observeAsState()
            val isStartGame = remember { trainingRunnerViewModel.runnerEngine.isStartGame }
            val score = remember { trainingRunnerViewModel.runnerEngine.score }
            val player = remember { trainingRunnerViewModel.runnerEngine.player }
            val hurdleList = remember { trainingRunnerViewModel.runnerEngine.hurdleList }

            TrainingNestedBackground(isMoving = isStartGame.value)

            mongVo.value?.let {
                player.value?.let { player ->
                    TrainingRunnerContent(
                        mongTypeCode = it.mongTypeCode,
                        player = player,
                        hurdleList = hurdleList,
                        modifier = Modifier.zIndex(1f),
                    )

                    TrainingRunnerInfoContent(
                        payPoint = score.value * runnerPayPoint.value,
                        modifier = Modifier.zIndex(2f),
                    )
                }

                /**
                 * Dialog
                 */
                if (trainingRunnerViewModel.uiState.trainingStartDialog) {
                    TrainingStartDialog(
                        firstText = "화면을 클릭하여",
                        secondText = "장애물을 뛰어넘기",
                        rewardPayPoint = runnerPayPoint.value,
                        trainingStart = { trainingRunnerViewModel.runnerStart() },
                        modifier = Modifier.zIndex(3f),
                    )
                } else if (trainingRunnerViewModel.uiState.trainingOverDialog) {
                    TrainingEndDialog(
                        trainingEnd = {
                            trainingRunnerViewModel.runnerEnd(
                                mongId = it.mongId,
                                score = score.value,
                            )
                        },
                        rewardPayPoint = score.value * runnerPayPoint.value,
                        trainingCode = TrainingCode.RUNNER,
                        modifier = Modifier.zIndex(3f),
                    )
                }
            }
        }
    }

    LaunchedEffect(trainingRunnerViewModel.uiState.navMainEvent) {
        trainingRunnerViewModel.uiState.navMainEvent.collect {
            navController.popBackStack(route = NavItem.TrainingJumping.route, inclusive = true)
        }
    }

    // 프레임 변경 시마다 재 랜더링
    LaunchedEffect(trainingRunnerViewModel.runnerEngine.playMillis.value) {}
}

@Composable
private fun TrainingRunnerContent(
    mongTypeCode: String,
    player: RunnerEngine.Player,
    hurdleList: List<RunnerEngine.Hurdle>,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { player.jump() },
            )
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxHeight()
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.72f)
            ) {
                Box(
                    contentAlignment = Alignment.BottomStart,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier.zIndex(1f)
                    ) {
                        hurdleList.forEachIndexed { index, hurdle ->
                            Hurdle(
                                image = R.drawable.poops,
                                height = hurdle.height,
                                width = hurdle.width,
                                modifier = Modifier
                                    .zIndex(-index.toFloat())
                                    .offset {
                                        IntOffset(
                                            y = (hurdle.py.value).dp.roundToPx(),
                                            x = (hurdle.px.value).dp.roundToPx(),
                                        )
                                    }
                            )
                        }
                    }

                    Box(
                        modifier = Modifier.zIndex(2f)
                    ) {
                        Player(
                            mongCode = mongTypeCode,
                            height = player.height,
                            width = player.width,
                            modifier = Modifier
                                .zIndex(1f)
                                .offset {
                                    IntOffset(
                                        y = (player.py.value).dp.roundToPx(),
                                        x = (player.px.value).dp.roundToPx(),
                                    )
                                }
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.28f)
            ) {
                Spacer(modifier = Modifier)
            }
        }
    }
}

@Composable
private fun TrainingRunnerInfoContent(
    payPoint: Int,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        PayPoint(
            payPoint = payPoint,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun Player(
    mongCode: String,
    height: Int,
    width: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(height.dp)
            .width(width.dp),
    ) {
        Image(
            painter = painterResource(MongResourceCode.valueOf(mongCode).pngCode),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    // 좌우 반전을 위해 scaleX -1로 설정
                    scaleX = -1f
                ),
            contentScale = ContentScale.FillBounds,
        )
    }
}

@Composable
private fun Hurdle(
    image: Int,
    height: Int,
    width: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(height.dp)
            .width(width.dp)
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f),
            contentScale = ContentScale.FillBounds,
        )
    }
}

@Composable
private fun TrainingRunnerLoadingBar(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        LoadingBar()
    }
}