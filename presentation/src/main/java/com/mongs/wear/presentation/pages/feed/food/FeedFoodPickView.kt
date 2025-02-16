package com.mongs.wear.presentation.pages.feed.food

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
import com.mongs.wear.presentation.component.background.FeedNestedBackground
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
fun FeedFoodPickView(
    navController: NavController,
    feedFoodPickViewModel: FeedFoodPickViewModel = hiltViewModel(),
) {

    val mongVo = feedFoodPickViewModel.mongVo.observeAsState()
    val feedItemVoList = feedFoodPickViewModel.foodVoList.observeAsState(ArrayList())

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
        if (feedFoodPickViewModel.uiState.loadingBar) {
            FeedNestedBackground()
            FeedFoodPickLoadingBar()
        } else if (feedFoodPickViewModel.uiState.buyDialog) {
            ConfirmAndCancelDialog(
                text = "구매하시겠습니까?",
                confirm = { feedFoodPickViewModel.buyFood(feedItemVoList.value[feedItemVoIndex.intValue].foodTypeCode) },
                cancel = { feedFoodPickViewModel.uiState.buyDialog = false }
            )
        } else {
            FeedNestedBackground()
            PageIndicator(
                pageIndicatorState = pageIndicatorState,
                modifier = Modifier.zIndex(1f)
            )

            FeedFoodPickContent(
                mongVo = mongVo.value,
                feedItemVo = feedItemVoList.value[feedItemVoIndex.intValue],
                detailDialogOpen = { feedFoodPickViewModel.uiState.detailDialog = true },
                buyDialogOpen = { feedFoodPickViewModel.uiState.buyDialog = true },
                modifier = Modifier.zIndex(2f),
            )

            SelectButton(
                leftBtnDisabled = feedItemVoIndex.intValue == 0,
                rightBtnDisabled = feedItemVoIndex.intValue == feedItemVoList.value.size - 1,
                leftBtnClick = { feedItemVoIndex.intValue = max(0, feedItemVoIndex.intValue - 1) },
                rightBtnClick = { feedItemVoIndex.intValue = min(feedItemVoIndex.intValue + 1, feedItemVoList.value.size - 1) },
                modifier = Modifier.zIndex(3f)
            )

            if (feedFoodPickViewModel.uiState.detailDialog) {
                FeedItemDetailDialog(
                    onClick = { feedFoodPickViewModel.uiState.detailDialog = false },
                    addWeightValue = feedItemVoList.value[feedItemVoIndex.intValue].addWeightValue,
                    addStrengthValue = feedItemVoList.value[feedItemVoIndex.intValue].addStrengthValue,
                    addSatietyValue = feedItemVoList.value[feedItemVoIndex.intValue].addSatietyValue,
                    addHealthyValue = feedItemVoList.value[feedItemVoIndex.intValue].addHealthyValue,
                    addFatigueValue = feedItemVoList.value[feedItemVoIndex.intValue].addFatigueValue,
                    modifier = Modifier.zIndex(3f)
                )
            }
        }

        LaunchedEffect(feedFoodPickViewModel.uiState.navMainEvent) {
            feedFoodPickViewModel.uiState.navMainEvent.collect {
                navController.popBackStack(route = NavItem.FeedNested.route, inclusive = true)
            }
        }

        LaunchedEffect(feedFoodPickViewModel.uiState.navFeedMenuEvent) {
            feedFoodPickViewModel.uiState.navFeedMenuEvent.collect {
                navController.popBackStack()
            }
        }
    }
}

@Composable
private fun FeedFoodPickContent(
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
private fun FeedFoodPickLoadingBar(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        LoadingBar()
    }
}

