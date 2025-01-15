package com.mongs.wear.presentation.dialog.slotPick

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.wear.compose.material.Text
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsLightGray
import com.mongs.wear.presentation.component.common.button.BlueButton
import com.mongs.wear.presentation.dialog.common.ConfirmAndCancelDialog
import com.mongs.wear.presentation.dialog.common.InputBox
import com.mongs.wear.presentation.dialog.common.TimePickerDialog

const val SLOT_ADD_ROUND_SIZE = 10
const val SLOT_ADD_WIDTH = 144
const val SLOT_ADD_HEIGHT = 130
const val SLOT_ADD_TAB_WIDTH = 144
const val SLOT_ADD_TAB_HEIGHT = 95
const val SLOT_ADD_BAR_WIDTH = 48
const val SLOT_ADD_BAR_HEIGHT = 32

@Composable
fun SlotAddDialog(
    add: (String, String, String) -> Unit,
    initTabIndex: Int = 0,
    context: Context = LocalContext.current,
) {
    val timePickerDialog = remember { mutableStateOf(false) }
    val timePickerFlag = remember { mutableIntStateOf(0) }
    val addDialog = remember { mutableStateOf(false) }

    val tabIndex = remember{ mutableIntStateOf(initTabIndex) }
    val name = remember { mutableStateOf("") }
    val sleepStartHour = remember { mutableIntStateOf(0) }
    val sleepStartMinute = remember { mutableIntStateOf(0) }
    val sleepEndHour = remember { mutableIntStateOf(0) }
    val sleepEndMinute = remember { mutableIntStateOf(0) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .zIndex(0f)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(width = SLOT_ADD_WIDTH.dp, height = SLOT_ADD_HEIGHT.dp)
                    .zIndex(1f)
                    .clip(RoundedCornerShape(SLOT_ADD_ROUND_SIZE.dp))
                    .background(color = Color.LightGray)
                    .align(Alignment.BottomCenter)
            )

            Box(
                modifier = Modifier
                    .size(width = SLOT_ADD_WIDTH.dp, height = SLOT_ADD_BAR_HEIGHT.dp)
                    .zIndex(2f)
                    .align(Alignment.TopCenter)
            ) {
                Row (
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    SlotAddTab(
                        text = "이름",
                        color = if (tabIndex.intValue == 0) MongsLightGray else Color.LightGray,
                        onClick = { tabIndex.intValue = 0 },
                    )
                    SlotAddTab(
                        text = "수면",
                        color = if (tabIndex.intValue == 1) MongsLightGray else Color.LightGray,
                        onClick = { tabIndex.intValue = 1 },
                    )
                    SlotAddTab(
                        text = "기상",
                        color = if (tabIndex.intValue == 2) MongsLightGray else Color.LightGray,
                        onClick = { tabIndex.intValue = 2 },
                    )
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(top = SLOT_ADD_BAR_HEIGHT.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = SLOT_ADD_ROUND_SIZE.dp,
                            bottomEnd = SLOT_ADD_ROUND_SIZE.dp
                        )
                    )
                    .background(color = MongsLightGray)
                    .size(
                        width = SLOT_ADD_TAB_WIDTH.dp,
                        height = (SLOT_ADD_TAB_HEIGHT - SLOT_ADD_BAR_HEIGHT).dp
                    )
                    .zIndex(2f)
                    .align(Alignment.TopCenter)
            ) {
                when (tabIndex.intValue) {
                    0 -> InputNameTab(
                        modifier = Modifier.matchParentSize(),
                        name = name.value,
                        changeInput = { inputName -> name.value = inputName },
                    )

                    1 -> InputTimeTab(
                        modifier = Modifier.matchParentSize(),
                        hour = sleepStartHour.intValue,
                        minute = sleepStartMinute.intValue,
                        hourTimePickerDialog = {
                            timePickerDialog.value = true
                            timePickerFlag.intValue = 0
                        },
                        minuteTimePickerDialog = {
                            timePickerDialog.value = true
                            timePickerFlag.intValue = 1
                        }
                    )

                    2 -> InputTimeTab(
                        modifier = Modifier.matchParentSize(),
                        hour = sleepEndHour.intValue,
                        minute = sleepEndMinute.intValue,
                        hourTimePickerDialog = {
                            timePickerDialog.value = true
                            timePickerFlag.intValue = 0
                        },
                        minuteTimePickerDialog = {
                            timePickerDialog.value = true
                            timePickerFlag.intValue = 1
                        }
                    )
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(
                        width = SLOT_ADD_WIDTH.dp,
                        height = (SLOT_ADD_HEIGHT - SLOT_ADD_TAB_HEIGHT).dp
                    )
                    .zIndex(2f)
                    .align(Alignment.BottomCenter)
            ) {
                BlueButton(
                    text = "생성",
                    onClick = {
                        if (name.value.length <= 6) {
                            addDialog.value = true
                        } else {
                            Toast.makeText(context, "이름은 6자 제한!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    height = 26,
                    disable = name.value.isBlank() || (sleepStartHour.intValue == sleepEndHour.intValue && sleepStartMinute.intValue == sleepEndMinute.intValue)
                )
            }
        }
    }

    if (timePickerDialog.value) {
        TimePickerDialog(
            initValue = when(timePickerFlag.intValue) {
                0 -> 12
                1 -> 0
                else -> 0
            },
            valueRange = when(timePickerFlag.intValue) {
                0 -> (0..23).toList()
                1 -> (0..59).toList()
                else -> emptyList()
            },
            confirm = when(timePickerFlag.intValue) {
                0 -> { value ->
                    when(tabIndex.intValue) {
                        1 -> { sleepStartHour.intValue = value }
                        2 -> { sleepEndHour.intValue = value }
                    }
                    timePickerDialog.value = false
                }

                1 -> { value ->
                    when(tabIndex.intValue) {
                        1 -> { sleepStartMinute.intValue = value }
                        2 -> { sleepEndMinute.intValue = value }
                    }
                    timePickerDialog.value = false
                }
                else -> { _ -> }
            },
        )
    }

    if (addDialog.value) {
        ConfirmAndCancelDialog(
            text = "새로운 몽을\n생성하시겠습니까?\n(시간은 수정불가)",
            confirm = {
                if (name.value.length <= 6) {
                    val sleepStart = "%02d:%02d".format(sleepStartHour.intValue, sleepStartMinute.intValue)
                    val sleepEnd = "%02d:%02d".format(sleepEndHour.intValue, sleepEndMinute.intValue)
                    add(name.value, sleepStart, sleepEnd)
                } else {
                    Toast.makeText(context, "이름은 6자 제한!", Toast.LENGTH_SHORT).show()
                }
            },
            cancel = {
                addDialog.value = false
            }
        )
    }
}

@Composable
private fun InputNameTab(
    modifier: Modifier = Modifier.fillMaxSize(),
    name: String,
    changeInput: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(8.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(SLOT_ADD_TAB_WIDTH.dp)
                .weight(0.2f),
        ) {
            InputBox(
                text = name,
                changeInput = { inputName -> changeInput(inputName) },
                maxLines = 1,
                placeholder = "최대 6자 입력"
            )
        }
    }
}

@Composable
private fun InputTimeTab(
    modifier: Modifier = Modifier.fillMaxSize(),
    hour: Int,
    minute: Int,
    hourTimePickerDialog: () -> Unit,
    minuteTimePickerDialog: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .size(38.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = hourTimePickerDialog,
                    ),
            ) {
                Text(
                    text = "$hour",
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    color = Color.Black,
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "시",
                textAlign = TextAlign.Center,
                fontFamily = DAL_MU_RI,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
                color = Color.Black,
            )

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .size(38.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = minuteTimePickerDialog,
                    ),
            ) {
                Text(
                    text = "$minute",
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    color = Color.Black,
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "분",
                textAlign = TextAlign.Center,
                fontFamily = DAL_MU_RI,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
                color = Color.Black,
            )
        }
    }
}

@Composable
private fun SlotAddTab(
    onClick: () -> Unit,
    color: Color,
    text: String,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topStart = SLOT_ADD_ROUND_SIZE.dp,
                    topEnd = SLOT_ADD_ROUND_SIZE.dp
                )
            )
            .size(width = SLOT_ADD_BAR_WIDTH.dp, height = SLOT_ADD_BAR_HEIGHT.dp)
            .background(color = color)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontFamily = DAL_MU_RI,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            color = Color.Black,
        )
    }
}

@Preview(showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun FirstSlotDetailDialogPreview() {
    SlotAddDialog(add = { name, sleepStart, sleepEnd -> }, initTabIndex = 0)
}

@Preview(showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun SecondEmptySlotDetailDialogPreview() {
    SlotAddDialog(add = { name, sleepStart, sleepEnd -> }, initTabIndex = 1)
}

@Preview(showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun SecondFullSlotDetailDialogPreview() {
    SlotAddDialog(add = { name, sleepStart, sleepEnd -> }, initTabIndex = 1)
}

@Preview(showSystemUi = true, device = Devices.WEAR_OS_LARGE_ROUND)
@Composable
private fun LargeSecondFullSlotDetailDialogPreview() {
    SlotAddDialog(add = { name, sleepStart, sleepEnd -> }, initTabIndex = 1)
}