package com.mongs.wear.presentation.pages.help.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Text
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.component.common.background.HelpNestedBackground
import com.mongs.wear.presentation.component.common.chip.Chip
import com.mongs.wear.presentation.dialog.help.HelpBattleDialog
import com.mongs.wear.presentation.dialog.help.HelpInfoDialog
import com.mongs.wear.presentation.dialog.help.HelpMongDialog
import com.mongs.wear.presentation.dialog.help.HelpPointDialog
import com.mongs.wear.presentation.dialog.help.HelpSlotDialog
import com.mongs.wear.presentation.dialog.help.HelpTrainingDialog

@Composable
fun HelpMenuView(
    helpMenuViewModel: HelpMenuViewModel = hiltViewModel()
) {

    Box {
        HelpNestedBackground()
        HelpMenuContent(
            helpInfoDialog = { helpMenuViewModel.uiState.helpInfoDialog = true },
            helpPointDialog = { helpMenuViewModel.uiState.helpPointDialog = true },
            helpMongDialog = { helpMenuViewModel.uiState.helpMongDialog = true },
            helpSlotDialog = { helpMenuViewModel.uiState.helpSlotDialog = true },
            helpBattleDialog = { helpMenuViewModel.uiState.helpBattleDialog = true },
            helpTrainingDialog = { helpMenuViewModel.uiState.helpTrainingDialog = true },
            modifier = Modifier.zIndex(1f)
        )

        if (helpMenuViewModel.uiState.helpInfoDialog) {
            HelpInfoDialog(
                cancel = { helpMenuViewModel.uiState.helpInfoDialog = false },
                modifier = Modifier.zIndex(2f),
            )
        } else if (helpMenuViewModel.uiState.helpPointDialog) {
            HelpPointDialog(
                cancel = { helpMenuViewModel.uiState.helpPointDialog = false },
                modifier = Modifier.zIndex(2f),
            )
        } else if (helpMenuViewModel.uiState.helpMongDialog) {
            HelpMongDialog(
                cancel = { helpMenuViewModel.uiState.helpMongDialog = false },
                modifier = Modifier.zIndex(2f),
            )
        } else if (helpMenuViewModel.uiState.helpSlotDialog) {
            HelpSlotDialog(
                cancel = { helpMenuViewModel.uiState.helpSlotDialog = false },
                modifier = Modifier.zIndex(2f),
            )
        } else if (helpMenuViewModel.uiState.helpBattleDialog) {
            HelpBattleDialog(
                cancel = { helpMenuViewModel.uiState.helpBattleDialog = false },
                modifier = Modifier.zIndex(2f),
            )
        } else if (helpMenuViewModel.uiState.helpTrainingDialog) {
            HelpTrainingDialog(
                cancel = { helpMenuViewModel.uiState.helpTrainingDialog = false },
                modifier = Modifier.zIndex(2f),
            )
        }
    }
}

@Composable
private fun HelpMenuContent(
    helpInfoDialog: () -> Unit,
    helpPointDialog: () -> Unit,
    helpMongDialog: () -> Unit,
    helpSlotDialog: () -> Unit,
    helpBattleDialog: () -> Unit,
    helpTrainingDialog: () -> Unit,
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
                        text = "도움말",
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
                Chip(
                    fontColor = Color.White,
                    backgroundColor = Color.Black,
                    label = "게임소개",
                    secondaryLabel = "진화조건,졸업시기,걸음보상",
                    onClick = helpInfoDialog,
                )
            }

            item {
                Chip(
                    fontColor = Color.White,
                    backgroundColor = Color.Black,
                    label = "포인트",
                    secondaryLabel = "페이/스타 포인트 획득 방법",
                    onClick = helpPointDialog,
                )
            }

            item {
                Chip(
                    fontColor = Color.White,
                    backgroundColor = Color.Black,
                    label = "캐릭터관리",
                    secondaryLabel = "쓰다듬기,먹이주기",
                    onClick = helpMongDialog,
                )
            }

            item {
                Chip(
                    fontColor = Color.White,
                    backgroundColor = Color.Black,
                    label = "슬롯관리",
                    secondaryLabel = "생성,삭제,졸업,슬롯추가",
                    onClick = helpSlotDialog,
                )
            }

            item {
                Chip(
                    fontColor = Color.White,
                    backgroundColor = Color.Black,
                    label = "배틀",
                    secondaryLabel = "매칭,매치,보상",
                    onClick = helpBattleDialog,
                )
            }

            item {
                Chip(
                    fontColor = Color.White,
                    backgroundColor = Color.Black,
                    label = "훈련 - Runner",
                    secondaryLabel = "플레이 방법,보상",
                    onClick = helpTrainingDialog,
                )
            }
        }
    }
}