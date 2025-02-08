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
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.wear.compose.material.Text
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsLightGray
import com.mongs.wear.presentation.component.common.button.BlueButton

@Composable
fun TimePickerDialog(
    initHour: Int = 0,
    initMinute: Int = 0,
    confirm: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hour = remember { mutableIntStateOf(initHour) }
    val minute = remember { mutableIntStateOf(initMinute) }

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
                Spacer(modifier = Modifier.width(25.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.45f),
                ) {
                    TimePicker(
                        initValue = hour.intValue,
                        valueRange = (0..23).toList(),
                        changeValue = { value -> hour.intValue = value },
                    )
                }


                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.1f),
                ) {
                    Text(
                        text = ":",
                        textAlign = TextAlign.Center,
                        fontFamily = DAL_MU_RI,
                        fontWeight = FontWeight.Light,
                        fontSize = 25.sp,
                        color = MongsLightGray,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.45f),
                ) {
                    TimePicker(
                        initValue = minute.intValue,
                        valueRange = (0..59).toList(),
                        changeValue = { value -> minute.intValue = value },
                    )
                }

                Spacer(modifier = Modifier.width(25.dp))
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
                    onClick = { confirm(hour.intValue, minute.intValue) },
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun ConfirmDialogPreview() {
    TimePickerDialog(
        confirm = { hour, minute -> }
    )
}