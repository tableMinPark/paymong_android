package com.mongs.wear.presentation.pages.slot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.PageIndicatorState
import com.mongs.wear.domain.management.vo.SlotVo
import com.mongs.wear.presentation.assets.NavItem
import com.mongs.wear.presentation.component.common.background.FeedNestedBackground
import com.mongs.wear.presentation.component.common.background.SlotPickBackground
import com.mongs.wear.presentation.component.common.bar.LoadingBar
import com.mongs.wear.presentation.component.common.button.SelectButton
import com.mongs.wear.presentation.component.common.pagenation.PageIndicator
import com.mongs.wear.presentation.component.slot.BuySlot
import com.mongs.wear.presentation.component.slot.EmptySlot
import com.mongs.wear.presentation.component.slot.Slot
import com.mongs.wear.presentation.dialog.common.ConfirmAndCancelDialog
import com.mongs.wear.presentation.dialog.slotPick.SlotAddDialog
import com.mongs.wear.presentation.dialog.slotPick.SlotDetailDialog
import kotlin.math.max
import kotlin.math.min

@Composable
fun SlotPickView(
    navController: NavController,
    slotPickViewModel: SlotPickViewModel = hiltViewModel(),
) {
    Box {
        if (slotPickViewModel.uiState.loadingBar) {
            FeedNestedBackground()
            SlotPickLoadingBar()
        } else {
            val starPoint = slotPickViewModel.starPoint.observeAsState(0)
            val buySlotPrice = slotPickViewModel.buySlotPrice.observeAsState(0)
            val slotVoList = slotPickViewModel.slotVoList.observeAsState(ArrayList())
            val mongVoListIndex = remember { mutableIntStateOf(0) }
            val pageIndicatorState: PageIndicatorState = remember {
                object : PageIndicatorState {
                    override val pageOffset: Float
                        get() = 0f
                    override val selectedPage: Int
                        get() = mongVoListIndex.intValue
                    override val pageCount: Int
                        get() = slotVoList.value.size
                }
            }

            val nowSlotVo = remember {
                derivedStateOf {
                    if (slotVoList.value.isNotEmpty()) {
                        slotVoList.value[mongVoListIndex.intValue]
                    } else {
                        SlotVo(code = SlotVo.SlotCode.EMPTY)
                    }
                }
            }

            if (slotPickViewModel.uiState.mongCreateDialog) {
                SlotPickBackground()
                SlotAddDialog(
                    addBtnClick = { name, sleepStart, sleepEnd ->
                        slotPickViewModel.createMong(name, sleepStart, sleepEnd)
                    },
                    closeBtnClick = {
                        slotPickViewModel.uiState.mongCreateDialog = false
                    }
                )
            } else if (slotPickViewModel.uiState.mongDeleteDialog) {
                ConfirmAndCancelDialog(
                    text = "현재 몽을\n삭제하시겠습니까?",
                    confirm = {
                        nowSlotVo.value.mongVo?.let { mongVo ->
                            slotPickViewModel.deleteMong(mongId = mongVo.mongId)
                        }
                    },
                    cancel = {
                        slotPickViewModel.uiState.mongDeleteDialog = false
                    }
                )
            } else if (slotPickViewModel.uiState.pickDialog) {
                ConfirmAndCancelDialog(
                    text = "현재 몽을\n선택하시겠습니까?",
                    confirm = {
                        nowSlotVo.value.mongVo?.let { mongVo ->
                            slotPickViewModel.pickMong(mongId = mongVo.mongId)
                        }
                    },
                    cancel = {
                        slotPickViewModel.uiState.pickDialog = false
                    }
                )
            } else if (slotPickViewModel.uiState.mongGraduateDialog) {
                ConfirmAndCancelDialog(
                    text = "현재 몽을\n졸업시키시겠습니까?",
                    confirm = {
                        nowSlotVo.value.mongVo?.let { mongVo ->
                            slotPickViewModel.graduateMong(mongId = mongVo.mongId)
                        }
                    },
                    cancel = {
                        slotPickViewModel.uiState.mongGraduateDialog = false
                    }
                )
            } else if (slotPickViewModel.uiState.buySlotDialog) {
                ConfirmAndCancelDialog(
                    text = "새로운 슬롯을\n구매하시겠습니까?",
                    confirm = {
                        slotPickViewModel.buySlot()
                    },
                    cancel = {
                        slotPickViewModel.uiState.buySlotDialog = false
                    }
                )
            } else if (slotPickViewModel.uiState.mongDetailDialog) {
                nowSlotVo.value.mongVo?.let { mongVo ->
                    SlotPickBackground()
                    SlotDetailDialog(
                        onClick = { slotPickViewModel.uiState.mongDetailDialog = false },
                        mongId = mongVo.mongId,
                        statusCode = mongVo.statusCode,
                        stateCode = mongVo.stateCode,
                        isSleep = mongVo.isSleeping,
                        weight = mongVo.weight,
                        healthy = mongVo.healthy,
                        satiety = mongVo.satiety,
                        strength = mongVo.strength,
                        sleep = mongVo.sleep,
                        payPoint = mongVo.payPoint,
                        born = mongVo.born,
                    )
                } ?: run {
                    slotPickViewModel.uiState.mongDetailDialog = false
                }
            } else {

                SlotPickBackground()
                PageIndicator(
                    pageIndicatorState = pageIndicatorState,
                    modifier = Modifier.zIndex(1f)
                )

                SlotPickContent(
                    slotVo = nowSlotVo.value,
                    starPoint = starPoint.value,
                    buySlotPrice = buySlotPrice.value,
                    uiState = slotPickViewModel.uiState,
                    modifier = Modifier.zIndex(2f)
                )

                SelectButton(
                    leftBtnDisabled = mongVoListIndex.intValue == 0,
                    rightBtnDisabled = mongVoListIndex.intValue == slotVoList.value.size - 1,
                    leftBtnClick = { mongVoListIndex.intValue = max(0, mongVoListIndex.intValue - 1) },
                    rightBtnClick = { mongVoListIndex.intValue = min(mongVoListIndex.intValue + 1, slotVoList.value.size - 1) },
                    modifier = Modifier.zIndex(3f)
                )
            }
        }
    }

    LaunchedEffect(slotPickViewModel.uiState.navMainEvent) {
        slotPickViewModel.uiState.navMainEvent.collect {
            navController.navigate(NavItem.MainPager.route) {
                popUpTo(navController.graph.id)
            }
        }
    }
}

@Composable
private fun SlotPickContent(
    slotVo: SlotVo,
    starPoint: Int,
    buySlotPrice: Int,
    uiState: SlotPickViewModel.UiState,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        when (slotVo.code) {
            SlotVo.SlotCode.EMPTY -> {
                EmptySlot(
                    mongCreateDialogOpen = { uiState.mongCreateDialog = true }
                )
            }

            SlotVo.SlotCode.EXISTS -> {
                slotVo.mongVo?.let { mongVo ->
                    Slot(
                        mongVo = mongVo,
                        mongDetailDialogOpen = { uiState.mongDetailDialog = true },
                        mongGraduateDialogOpen = { uiState.mongGraduateDialog = true },
                        mongDeleteDialogOpen = { uiState.mongDeleteDialog = true },
                        mongPickDialogOpen = { uiState.pickDialog = true },
                    )
                }
            }

            SlotVo.SlotCode.BUY_SLOT -> {
                BuySlot(
                    starPoint = starPoint,
                    buySlotPrice = buySlotPrice,
                    buySlotDialogOpen = { uiState.buySlotDialog = true },
                )
            }
        }
    }
}


@Composable
private fun SlotPickLoadingBar(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        LoadingBar()
    }
}