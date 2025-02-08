package com.mongs.wear.presentation.dialog.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.mongs.wear.presentation.assets.MongsDarkGray
import com.mongs.wear.presentation.assets.MongsLightGray

@Composable
fun TimePicker(
    initValue: Int = 0,
    valueRange: List<Int>,
    changeValue: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val listState = rememberScalingLazyListState(initialCenterItemIndex = initValue)

    val isScrollInProgress = remember { derivedStateOf { listState.isScrollInProgress } }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .zIndex(0f)
    ) {
        Box(
            modifier = Modifier
                .zIndex(0f)
                .size(width = 65.dp, height = 30.dp)
                .background(
                    color = Color.DarkGray.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(size = 18.dp)
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
                    text = it.toString().padStart(2, '0'),
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 25.sp,
                    color = if (it == listState.centerItemIndex) MongsLightGray else MongsDarkGray,
                )
            }
        }
    }

    LaunchedEffect(isScrollInProgress.value) {
        if (!isScrollInProgress.value) {
            listState.animateScrollToItem(index = listState.centerItemIndex)
        }
    }

    LaunchedEffect(listState.centerItemIndex) {
        changeValue(valueRange[listState.centerItemIndex])
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun ConfirmDialogPreview() {
    TimePicker(
        valueRange = (0..23).toList(),
        changeValue = { value -> }
    )
}