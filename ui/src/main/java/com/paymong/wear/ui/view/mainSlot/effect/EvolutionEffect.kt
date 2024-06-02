package com.paymong.wear.ui.view.mainSlot.effect

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.wear.compose.material.Text
import com.paymong.wear.domain.vo.SlotVo
import com.paymong.wear.ui.R
import com.paymong.wear.ui.global.component.background.MainPagerBackground
import com.paymong.wear.ui.global.component.common.Mong
import com.paymong.wear.ui.global.resource.MongResourceCode
import com.paymong.wear.ui.global.theme.DAL_MU_RI
import com.paymong.wear.ui.global.theme.PaymongWhite
import kotlinx.coroutines.delay

private val imageList = listOf(
    R.drawable.create_effect_1,
    R.drawable.create_effect_2,
    R.drawable.create_effect_3,
)
private val delayList = listOf(
    100L,
    300L,
    300L,
    300L
)

@Composable
fun EvolutionEffect(
    slotVo: SlotVo,
    isEvolution: Boolean,
    runEvolution: () -> Unit = {},
    evolution: () -> Unit,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    if (isEvolution) {
        var nowStep by remember { mutableIntStateOf(0) }

        LaunchedEffect(Unit) {
            for (step in 0..2) {
                nowStep = step
                delay(delayList[step])
            }
            evolution()
        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = modifier.fillMaxSize(),
        ) {
            Image(
                modifier = Modifier.fillMaxWidth().height(160.dp),
                painter = painterResource(imageList[nowStep]),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.6f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = runEvolution
                )
        ) {
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .zIndex(1f)
                    .fillMaxSize()
            ) {
                Mong(
                    mong = MongResourceCode.valueOf(slotVo.mongCode),
                    modifier = Modifier.padding(bottom = 25.dp)
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .zIndex(2f)
                    .background(color = Color.Black.copy(alpha = 0.6f))
                    .fillMaxSize()
            ) {
                Text(
                    text = "진화를 위해\n\n화면을 터치해주세요",
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                    color = PaymongWhite,
                )
            }
        }
    }
}

@Preview(showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun EvolutionEffectPreview() {
    MainPagerBackground()
    EvolutionEffect(slotVo = SlotVo(), isEvolution = false, evolution = {} , runEvolution = {})
}

