package com.mongs.wear.presentation.component.common.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun SelectButton(
    leftBtnDisabled: Boolean = false,
    rightBtnDisabled: Boolean = false,
    leftBtnClick: () -> Unit,
    rightBtnClick: () -> Unit,
    modifier: Modifier = Modifier.zIndex(0f),
    content: @Composable () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {

                Spacer(modifier = Modifier.width(15.dp))

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.15f)
                ) {
                    if (!leftBtnDisabled) {
                        LeftButton(onClick = leftBtnClick)
                    }
                }

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.7f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        content()
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.15f)
                ) {
                    if (!rightBtnDisabled) {
                        RightButton(onClick = rightBtnClick)
                    }
                }

                Spacer(modifier = Modifier.width(15.dp))
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun RightButtonPreview() {
    SelectButton(
        leftBtnClick = {},
        rightBtnClick = {},
    )
}
