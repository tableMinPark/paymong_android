package com.mongs.wear.presentation.dialog.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Text
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsLightGray
import com.mongs.wear.presentation.component.common.button.BlueButton

@Composable
fun TimePickerDialog(
    initValue: Int = 0,
    valueRange: List<Int>,
    confirm: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val listState = rememberScalingLazyListState(initialCenterItemIndex = initValue)

    val isScrollInProgress = remember { derivedStateOf { listState.isScrollInProgress } }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = Color.Black)
            .fillMaxSize(fraction = 1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
            )
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(0f)
                ) {
                    Box(
                        modifier = Modifier
                            .zIndex(0f)
                            .size(width = 100.dp, height = 40.dp)
                            .background(
                                color = Color.DarkGray.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(size = 20.dp)
                            )
                    )

                    ScalingLazyColumn(
                        modifier = Modifier
                            .background(color = Color.Transparent)
                            .zIndex(1f),
                        state = listState,
                        autoCentering = AutoCenteringParams(0),
                    ) {
                        items(items = valueRange) {
                            Text(
                                modifier = Modifier
                                    .size(width = 100.dp, height = 40.dp)
                                    .padding(3.dp)
                                    .offset(x = 1.dp, y = 3.dp),
                                text = it.toString().padStart(2, '0'),
                                textAlign = TextAlign.Center,
                                fontFamily = DAL_MU_RI,
                                fontWeight = FontWeight.Light,
                                fontSize = 30.sp,
                                color = MongsLightGray,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
            ) {
                BlueButton(
                    text = "설정",
                    onClick = {
                        confirm(valueRange[listState.centerItemIndex])
                    },
                )
            }
        }
    }

    LaunchedEffect(isScrollInProgress.value) {
        if (!isScrollInProgress.value) {
            listState.animateScrollToItem(index = listState.centerItemIndex)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun ConfirmDialogPreview() {
    TimePickerDialog(valueRange = (0..23).toList())
}