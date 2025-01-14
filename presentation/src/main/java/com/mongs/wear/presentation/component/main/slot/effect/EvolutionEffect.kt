package com.mongs.wear.presentation.component.main.slot.effect

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.wear.compose.material.Text
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongResourceCode
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.component.common.charactor.Mong
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
    400L,
)

@Composable
fun EvolutionEffect(
    mongVo: MongVo,
    evolutionState: Boolean,
    evolutionStart: () -> Unit,
    evolution: (Long) -> Unit,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    if (!evolutionState) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.6f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = evolutionStart
                )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxHeight()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "진화를 위해",
                        textAlign = TextAlign.Center,
                        fontFamily = DAL_MU_RI,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        color = MongsWhite,
                    )

                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "화면을 터치해주세요.",
                        textAlign = TextAlign.Center,
                        fontFamily = DAL_MU_RI,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        color = MongsWhite,
                    )
                }
            }
        }
    } else {

        var nowStep by remember { mutableIntStateOf(0) }

        LaunchedEffect(Unit) {

            for (step in 0..2) {
                nowStep = step
                delay(delayList[step])
            }

            evolution(mongVo.mongId)
        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = modifier.fillMaxSize(),
        ) {

            Mong(
                mong = MongResourceCode.valueOf(mongVo.mongTypeCode),
                modifier = Modifier.padding(bottom = 25.dp),
                isPng = true,
            )

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                painter = painterResource(imageList[nowStep]),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        }
    }
}
