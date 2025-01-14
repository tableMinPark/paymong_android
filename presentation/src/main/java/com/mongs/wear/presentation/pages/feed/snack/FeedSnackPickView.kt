package com.mongs.wear.presentation.pages.feed.snack

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
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.PageIndicatorState
import androidx.wear.compose.material.Text
import com.mongs.wear.domain.management.vo.FeedItemVo
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.FeedResourceCode
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.assets.NavItem
import com.mongs.wear.presentation.component.common.background.FeedNestedBackground
import com.mongs.wear.presentation.component.common.bar.LoadingBar
import com.mongs.wear.presentation.component.common.button.BlueButton
import com.mongs.wear.presentation.component.common.button.SelectButton
import com.mongs.wear.presentation.component.common.pagenation.PageIndicator
import com.mongs.wear.presentation.component.common.textbox.PayPoint
import com.mongs.wear.presentation.dialog.common.ConfirmAndCancelDialog
import com.mongs.wear.presentation.dialog.feed.FeedItemDetailDialog
import kotlin.math.max
import kotlin.math.min

@Composable
fun FeedSnackPickView(
    navController: NavController,
    feedSnackPickViewModel: FeedSnackPickViewModel = hiltViewModel(),
) {

    val mongVo = feedSnackPickViewModel.mongVo.observeAsState()
    val feedItemVoList = feedSnackPickViewModel.snackVoList.observeAsState(ArrayList())

    val feedItemVoIndex = remember { mutableIntStateOf(0) }
    val pageIndicatorState: PageIndicatorState = remember {
        object : PageIndicatorState {
            override val pageOffset: Float
                get() = 0f
            override val selectedPage: Int
                get() = feedItemVoIndex.intValue
            override val pageCount: Int
                get() = feedItemVoList.value.size
        }
    }

    Box {
        if (feedSnackPickViewModel.uiState.loadingBar) {
            FeedNestedBackground()
            FeedSnackPickLoadingBar()
        } else if (feedSnackPickViewModel.uiState.buyDialog) {
            ConfirmAndCancelDialog(
                text = "구매하시겠습니까?",
                confirm = { feedSnackPickViewModel.buySnack(foodTypeCode = feedItemVoList.value[feedItemVoIndex.intValue].foodTypeCode) },
                cancel = { feedSnackPickViewModel.uiState.buyDialog = false }
            )
        } else if (feedSnackPickViewModel.uiState.detailDialog) {
            FeedItemDetailDialog(
                onClick = { feedSnackPickViewModel.uiState.detailDialog = false },
                addWeightValue = feedItemVoList.value[feedItemVoIndex.intValue].addWeightValue,
                addStrengthValue = feedItemVoList.value[feedItemVoIndex.intValue].addStrengthValue,
                addSatietyValue = feedItemVoList.value[feedItemVoIndex.intValue].addSatietyValue,
                addHealthyValue = feedItemVoList.value[feedItemVoIndex.intValue].addHealthyValue,
                addFatigueValue = feedItemVoList.value[feedItemVoIndex.intValue].addFatigueValue,
                modifier = Modifier.zIndex(3f)
            )
        } else {
            FeedNestedBackground()
            PageIndicator(
                pageIndicatorState = pageIndicatorState,
                modifier = Modifier.zIndex(1f)
            )

            FeedSnackPickContent(
                mongVo = mongVo.value,
                feedItemVo = feedItemVoList.value[feedItemVoIndex.intValue],
                detailDialogOpen = { feedSnackPickViewModel.uiState.detailDialog = true },
                buyDialogOpen = { feedSnackPickViewModel.uiState.buyDialog = true },
                modifier = Modifier.zIndex(2f),
            )

            SelectButton(
                leftButtonClick = { feedItemVoIndex.intValue = max(0, feedItemVoIndex.intValue - 1) },
                rightButtonClick = { feedItemVoIndex.intValue = min(feedItemVoIndex.intValue + 1, feedItemVoList.value.size - 1) },
                modifier = Modifier.zIndex(3f)
            )
        }
    }

    LaunchedEffect(feedSnackPickViewModel.uiState.navMainEvent) {
        feedSnackPickViewModel.uiState.navMainEvent.collect {
            navController.popBackStack(route = NavItem.FeedNested.route, inclusive = true)
        }
    }

    LaunchedEffect(feedSnackPickViewModel.uiState.navFeedMenuEvent) {
        feedSnackPickViewModel.uiState.navFeedMenuEvent.collect {
            navController.popBackStack()
        }
    }
}

@Composable
private fun FeedSnackPickContent(
    mongVo: MongVo?,
    feedItemVo: FeedItemVo,
    detailDialogOpen: () -> Unit,
    buyDialogOpen: () -> Unit,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.height(15.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
            ) {
                PayPoint(payPoint = mongVo?.payPoint ?: 0)
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.52f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.6f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f)
                    ) {
                        Text(
                            text = feedItemVo.foodTypeName,
                            textAlign = TextAlign.Center,
                            fontFamily = DAL_MU_RI,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp,
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
                        Image(
                            painter = painterResource(FeedResourceCode.valueOf(feedItemVo.foodTypeCode).code),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = detailDialogOpen,
                                ),
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.28f)
            ) {
                BlueButton(
                    text = "$${feedItemVo.price}",
                    width = 70,
                    disable = mongVo?.let { feedItemVo.price > mongVo.payPoint || !feedItemVo.isCanBuy } ?: true,
                    onClick = buyDialogOpen,
                )
            }

            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}


@Composable
private fun FeedSnackPickLoadingBar(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        LoadingBar()
    }
}


