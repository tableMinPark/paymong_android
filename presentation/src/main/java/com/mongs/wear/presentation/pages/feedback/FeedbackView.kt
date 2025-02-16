package com.mongs.wear.presentation.pages.feedback

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.mongs.wear.core.enums.FeedbackCode
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.component.background.FeedbackBackground
import com.mongs.wear.presentation.component.common.bar.LoadingBar
import com.mongs.wear.presentation.component.common.chip.Chip
import com.mongs.wear.presentation.dialog.common.ConfirmAndCancelDialog
import com.mongs.wear.presentation.dialog.common.ConfirmDialog
import com.mongs.wear.presentation.dialog.feedback.FeedbackAddDialog

@Composable
fun FeedbackView(
    feedbackViewModel: FeedbackViewModel = hiltViewModel()
) {
    val feedbackCode = remember { mutableStateOf(FeedbackCode.COMMON) }

    val content = remember { mutableStateOf("") }

    Box {
        if (feedbackViewModel.uiState.loadingBar) {
            FeedbackBackground()
            FeedbackLoadingBar()
        } else {
            FeedbackBackground()
            FeedbackContent(
                feedbackDialog = { selectCode ->
                    feedbackCode.value = selectCode
                    feedbackViewModel.uiState.inputDialog = true
                },
                modifier = Modifier.zIndex(1f)
            )

            if(feedbackViewModel.uiState.inputDialog) {
                FeedbackAddDialog(
                    feedbackCode = feedbackCode.value,
                    text = content.value,
                    changeText = { content.value = it },
                    confirm = {
                        feedbackViewModel.uiState.inputDialog = false
                        feedbackViewModel.uiState.addDialog = true
                    },
                    cancel = {
                        feedbackViewModel.uiState.inputDialog = false
                        content.value = ""
                    },
                    modifier = Modifier.zIndex(2f),
                )

            } else if (feedbackViewModel.uiState.addDialog) {
                ConfirmAndCancelDialog(
                    text = "\"${feedbackCode.value.message}\" 오류를\n전송 하시겠습니까?",
                    confirm = {
                        feedbackViewModel.createFeedback(
                            feedbackCode = feedbackCode.value,
                            content = content.value)

                        content.value = ""
                    },
                    cancel = {
                        feedbackViewModel.uiState.inputDialog = true
                        feedbackViewModel.uiState.addDialog = false
                        content.value = ""
                    },
                    modifier = Modifier.zIndex(2f),
                )
            } else if (feedbackViewModel.uiState.confirmDialog) {
                ConfirmDialog(
                    text = "신고되었습니다.\n처리결과는 메일로\n전달드리겠습니다.",
                    confirm = { feedbackViewModel.uiState.confirmDialog = false },
                    modifier = Modifier.zIndex(2f),
                )
            }
        }
    }
}

@Composable
private fun FeedbackContent(
    feedbackDialog: (FeedbackCode) -> Unit,
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
                        text = "오류신고",
                        textAlign = TextAlign.Center,
                        fontFamily = DAL_MU_RI,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        color = MongsWhite,
                        maxLines = 1,
                    )
                }
            }

            for (feedbackCode in FeedbackCode.entries) {
                item {
                    Chip(
                        fontColor = Color.White,
                        backgroundColor = Color.Black,
                        label = feedbackCode.message,
                        secondaryLabel = feedbackCode.secondaryMessage,
                        onClick = { feedbackDialog(feedbackCode) },
                    )
                }
            }
        }
    }
}

@Composable
private fun FeedbackLoadingBar(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        LoadingBar()
    }
}