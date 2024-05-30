package com.paymong.wear.ui.global.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import com.paymong.wear.ui.R
import com.paymong.wear.ui.global.component.common.PayPoint
import com.paymong.wear.ui.global.theme.DAL_MU_RI
import com.paymong.wear.ui.global.theme.PaymongBlue
import com.paymong.wear.ui.global.theme.PaymongGreen
import com.paymong.wear.ui.global.theme.PaymongLightGray
import com.paymong.wear.ui.global.theme.PaymongPink
import com.paymong.wear.ui.global.theme.PaymongYellow
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.round

const val ROUND_SIZE = 10
const val SLOT_DETAIL_WIDTH = 140
const val SLOT_DETAIL_HEIGHT = 130
const val SLOT_DETAIL_BAR_HEIGHT = 30

@Composable
fun SlotDetailDialog(
    onClick: () -> Unit = {},
    initTabIndex: Int = 0,
    mongId: Long = 0L,
    name: String = "이름 없음",
    weight: Double = 0.0,
    healthy: Double = 0.0,
    satiety: Double = 0.0,
    strength: Double = 0.0,
    sleep: Double = 0.0,
    payPoint: Int = 0,
    born: LocalDateTime = LocalDateTime.now().minusDays(1).plusSeconds(1),
) {
    val tabIndex = remember{ mutableIntStateOf(initTabIndex) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .zIndex(0f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(width = SLOT_DETAIL_WIDTH.dp, height = SLOT_DETAIL_HEIGHT.dp)
                    .zIndex(1f)
                    .clip(RoundedCornerShape(ROUND_SIZE.dp))
                    .background(color = Color.LightGray)
                    .align(Alignment.BottomCenter)
            )

            Box(
                modifier = Modifier
                    .size(width = SLOT_DETAIL_WIDTH.dp, height = SLOT_DETAIL_BAR_HEIGHT.dp)
                    .zIndex(2f)
                    .align(Alignment.TopStart)
            ) {
                Row (
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    SlotDetailTab(
                        text = "정보",
                        color = if (tabIndex.intValue == 0) PaymongLightGray else Color.LightGray,
                        onClick = { tabIndex.intValue = 0 },
                    )
                    SlotDetailTab(
                        text = "지수",
                        color = if (tabIndex.intValue == 1) PaymongLightGray else Color.LightGray,
                        onClick = { tabIndex.intValue = 1 },
                    )
                    SlotDetailTab(
                        text = "포인트",
                        color = if (tabIndex.intValue == 2) PaymongLightGray else Color.LightGray,
                        onClick = { tabIndex.intValue = 2 },
                    )
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            bottomStart = ROUND_SIZE.dp,
                            bottomEnd = ROUND_SIZE.dp
                        )
                    )
                    .background(color = PaymongLightGray)
                    .size(width = SLOT_DETAIL_WIDTH.dp, height = (SLOT_DETAIL_HEIGHT - SLOT_DETAIL_BAR_HEIGHT).dp)
                    .zIndex(2f)
                    .align(Alignment.BottomCenter)
            ) {
                when (tabIndex.intValue) {
                    0 -> InfoTab(
                        modifier = Modifier.matchParentSize(),
                        mongId = mongId,
                        name = name,
                        born = born,
                        weight = weight,
                    )
                    1 -> StatusTab(
                        modifier = Modifier.matchParentSize(),
                        healthy = healthy,
                        satiety = satiety,
                        strength = strength,
                        sleep = sleep,
                    )
                    2 -> PointTab(
                        modifier = Modifier.matchParentSize(),
                        payPoint = payPoint,
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoTab(
    modifier: Modifier = Modifier.fillMaxSize(),
    mongId: Long,
    name: String,
    born: LocalDateTime,
    weight: Double,
) {
    val age = remember { mutableStateOf("0시간 0분 0초") }
    LaunchedEffect(mongId) {
        while(true) {
            val now = LocalDateTime.now()
            val hours = ChronoUnit.HOURS.between(born, now)
            val minutes = ChronoUnit.MINUTES.between(born.plusHours(hours), now)
            val seconds = ChronoUnit.SECONDS.between(born.plusHours(hours).plusMinutes(minutes), now)
            age.value = "${hours}시간 ${minutes}분 ${seconds}초"
            delay(1000)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(8.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(SLOT_DETAIL_WIDTH.dp)
                .weight(0.2f)
        ) {
            Text(
                text = name,
                textAlign = TextAlign.Center,
                fontFamily = DAL_MU_RI,
                fontWeight = FontWeight.Light,
                fontSize = 13.sp,
                maxLines = 1,
                color = Color.Black,
            )
        }
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(SLOT_DETAIL_WIDTH.dp)
                .weight(0.2f)
        ) {
            Text(
                text = age.value,
                textAlign = TextAlign.Center,
                fontFamily = DAL_MU_RI,
                fontWeight = FontWeight.Light,
                fontSize = 13.sp,
                maxLines = 1,
                color = Color.Black,
            )
        }
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(SLOT_DETAIL_WIDTH.dp)
                .weight(0.2f)
        ) {
            Text(
                text = "${round(weight * 10) / 10} g",
                textAlign = TextAlign.Center,
                fontFamily = DAL_MU_RI,
                fontWeight = FontWeight.Light,
                fontSize = 13.sp,
                maxLines = 1,
                color = Color.Black,
            )
        }
    }
}

@Composable
private fun StatusTab(
    modifier: Modifier = Modifier.fillMaxSize(),
    healthy: Double,
    satiety: Double,
    strength: Double,
    sleep: Double,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        Row {
            SlotDetailCondition(
                icon = R.drawable.health,
                progress = healthy.toFloat(),
                indicatorColor = PaymongPink
            )
            SlotDetailCondition(
                icon = R.drawable.satiety,
                progress = satiety.toFloat(),
                indicatorColor = PaymongYellow
            )
        }
        Row{
            SlotDetailCondition(
                icon = R.drawable.strength,
                progress = strength.toFloat(),
                indicatorColor = PaymongGreen
            )
            SlotDetailCondition(
                icon = R.drawable.sleep,
                progress = sleep.toFloat(),
                indicatorColor = PaymongBlue
            )
        }
    }
}

@Composable
private fun PointTab(
    modifier: Modifier = Modifier.fillMaxSize(),
    payPoint: Int,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        Row {
            PayPoint(payPoint = payPoint)
        }
    }
}

@Composable
private fun SlotDetailCondition(
    icon: Int,
    progress: Float,
    indicatorColor: Color
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(5.dp)
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )

        CircularProgressIndicator(
            modifier = Modifier.size(36.dp),
            trackColor = Color.LightGray,
            startAngle = 271f,
            endAngle = 270f,
            progress = progress / 100,
            strokeWidth = 2.dp,
            indicatorColor = indicatorColor,
        )
    }
}

@Composable
private fun SlotDetailTab(
    onClick: () -> Unit,
    color: Color,
    text: String,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = ROUND_SIZE.dp, topEnd = ROUND_SIZE.dp))
            .size(width = 42.dp, height = 30.dp)
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
            fontSize = 11.sp,
            color = Color.Black,
        )
    }
}

@Preview(showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun FirstSlotDetailDialogPreview() {
    SlotDetailDialog(initTabIndex = 0)
}

@Preview(showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun SecondEmptySlotDetailDialogPreview() {
    SlotDetailDialog(initTabIndex = 1)
}

@Preview(showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun SecondFullSlotDetailDialogPreview() {
    SlotDetailDialog(initTabIndex = 1, healthy = 100.0, strength = 100.0, satiety = 100.0, sleep = 100.0)
}

@Preview(showSystemUi = true, device = Devices.WEAR_OS_LARGE_ROUND)
@Composable
private fun LargeSecondFullSlotDetailDialogPreview() {
    SlotDetailDialog(initTabIndex = 1, healthy = 100.0, strength = 100.0, satiety = 100.0, sleep = 100.0)
}

@Preview(showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun ThirdSlotDetailDialogPreview() {
    SlotDetailDialog(initTabIndex = 2, payPoint = 100)
}