package com.mongs.wear.presentation.pages.main.slot

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mongs.wear.core.enums.MongStateCode
import com.mongs.wear.core.errors.PresentationErrorCode
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.assets.MongResourceCode
import com.mongs.wear.presentation.assets.NavItem
import com.mongs.wear.presentation.component.main.slot.content.DeadContent
import com.mongs.wear.presentation.component.main.slot.content.DeleteContent
import com.mongs.wear.presentation.component.main.slot.content.EmptyContent
import com.mongs.wear.presentation.component.main.slot.content.GraduatedContent
import com.mongs.wear.presentation.component.main.slot.content.NormalContent
import com.mongs.wear.presentation.component.main.slot.effect.EvolutionEffect
import com.mongs.wear.presentation.component.main.slot.effect.GraduatedEffect
import com.mongs.wear.presentation.component.main.slot.effect.GraduationEffect
import com.mongs.wear.presentation.component.main.slot.effect.HeartEffect
import com.mongs.wear.presentation.component.main.slot.effect.PoopCleanEffect
import com.mongs.wear.presentation.component.main.slot.effect.PoopEffect
import com.mongs.wear.presentation.component.main.slot.effect.SleepEffect
import com.mongs.wear.presentation.dialog.slot.SlotInteractionDialog
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import com.mongs.wear.presentation.pages.main.slot.MainSlotViewModel.UiState

@Composable
fun MainSlotView(
    navController: NavController,
    mongVo: MongVo?,
    isPageChanging: State<Boolean>,
    mainSlotViewModel: MainSlotViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
) {
    Box {
        MainSlotContent(
            mongVo = mongVo,
            isPageChanging = isPageChanging.value,
            onMongClick = {
                mainSlotViewModel.uiState.slotInteractionDialog = true
            },
            navSlotPick = { navController.navigate(NavItem.SlotPick.route) },
            modifier = Modifier.zIndex(1f),
            uiState = mainSlotViewModel.uiState,
        )

        mongVo?.let {
            MainSlotEffect(
                mongVo = mongVo,
                isPageChanging = isPageChanging.value,
                evolution = { mongId ->
                    mainSlotViewModel.evolution(mongId)
                },
                graduationReady = {
                    mainSlotViewModel.graduationReady(mongId = mongVo.mongId)
                },
                modifier = Modifier.zIndex(2f),
                uiState = mainSlotViewModel.uiState,
            )

            if (mainSlotViewModel.uiState.slotInteractionDialog && !isPageChanging.value) {
                SlotInteractionDialog(
                    mongVo = mongVo,
                    feed = {
                        mainSlotViewModel.uiState.slotInteractionDialog = false
                        navController.navigate(NavItem.FeedNested.route)
                    },
                    slotPick = {
                        mainSlotViewModel.uiState.slotInteractionDialog = false
                        navController.navigate(NavItem.SlotPick.route)
                    },
                    sleeping = {
                        mainSlotViewModel.sleeping(mongId = mongVo.mongId)
                    },
                    exchangeWalking = {
                        mainSlotViewModel.uiState.slotInteractionDialog = false
                        navController.navigate(NavItem.ExchangeWalking.route)
                    },
                    poopClean = {
                        mainSlotViewModel.poopClean(mongId = mongVo.mongId)
                    },
                    inventory = {
                        mainSlotViewModel.uiState.slotInteractionDialog = false
                        navController.navigate(NavItem.Inventory.route)
                    },
                    stroke = {
                        mainSlotViewModel.stroke(mongId = mongVo.mongId)
                    },
                    close = { mainSlotViewModel.uiState.slotInteractionDialog = false },
                    modifier = Modifier.zIndex(3f)
                )
            }

            LaunchedEffect(isPageChanging.value) {
                if (isPageChanging.value) {
                    mainSlotViewModel.uiState.slotInteractionDialog = false
                }
            }
        }
    }
}

@Composable
private fun MainSlotContent(
    mongVo: MongVo?,
    isPageChanging: Boolean,
    onMongClick: () -> Unit,
    navSlotPick: () -> Unit,
    modifier: Modifier = Modifier.zIndex(0f),
    uiState: UiState,
) {
    mongVo?.let {
        when (mongVo.stateCode) {
            MongStateCode.NORMAL -> {
                NormalContent(
                    mongVo = mongVo,
                    onMongClick = onMongClick,
                    modifier = modifier,
                )
            }

            MongStateCode.DEAD -> {
                DeadContent(
                    modifier = modifier,
                )
            }

            MongStateCode.GRADUATE_READY -> {
                NormalContent(
                    mongVo = mongVo,
                    onMongClick = onMongClick,
                    modifier = modifier,
                )
            }

            MongStateCode.EVOLUTION_READY -> {
                if (!uiState.isEvolution) {
                    NormalContent(
                        mongVo = mongVo,
                        onMongClick = {},
                        modifier = modifier,
                    )
                }
            }

            MongStateCode.GRADUATE -> {
                GraduatedContent(
                    mongVo = mongVo,
                    isPageChanging = isPageChanging,
                    modifier = modifier,
                )
            }

            MongStateCode.DELETE -> {
                DeleteContent(
                    isPageChanging = isPageChanging,
                    onClick = {},
                    modifier = modifier,
                )
            }

            else -> {}
        }
    } ?: run {
        EmptyContent(
            onClick = navSlotPick,
            modifier = modifier,
        )
    }
}

@Composable
private fun MainSlotEffect(
    mongVo: MongVo,
    isPageChanging: Boolean,
    evolution: (Long) -> Unit,
    graduationReady: () -> Unit,
    modifier: Modifier = Modifier.zIndex(0f),
    uiState: UiState,
) {
    when (mongVo.stateCode) {

        MongStateCode.NORMAL -> {
            if (BaseViewModel.effectState.isHappy) {
                HeartEffect(modifier = modifier)
            } else if (BaseViewModel.effectState.isPoopCleaning) {
                PoopCleanEffect(modifier = modifier)
            } else if (mongVo.isSleeping) {
                SleepEffect(modifier = modifier)
            }

            PoopEffect(
                poopCount = mongVo.poopCount,
                modifier = modifier,
            )
        }

        MongStateCode.DEAD -> {}

        MongStateCode.GRADUATE_READY -> {
            if (!isPageChanging && !mongVo.graduateCheck) {
                GraduationEffect(
                    graduationReady = graduationReady,
                    modifier = modifier,
                )
            } else if(!isPageChanging) {
                GraduatedEffect(
                    modifier = modifier,
                )
            }
        }

        MongStateCode.EVOLUTION_READY -> {

            if (!isPageChanging && !mongVo.isSleeping) {
                EvolutionEffect(
                    mongVo = mongVo,
                    isEvolution = uiState.isEvolution,
                    evolutionStart = { uiState.isEvolution = true },
                    evolution = { mongId -> evolution(mongId) },
                    modifier = modifier,
                )
            }
        }

        MongStateCode.GRADUATE -> {}

        MongStateCode.DELETE -> {}

        else -> {}
    }
}
