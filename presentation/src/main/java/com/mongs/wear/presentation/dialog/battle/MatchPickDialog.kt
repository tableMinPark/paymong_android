package com.mongs.wear.presentation.dialog.battle

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.wear.compose.material.Text
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.component.background.BattleMenuBackground
import com.mongs.wear.presentation.component.common.bar.ProgressIndicator
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun MatchPickDialog(
    attack: () -> Unit,
    defence: () -> Unit,
    heal: () -> Unit,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(progress) {
        if (progress >= 100f) {
            when (Random.nextInt(3)) {
                0 -> { attack() }
                1 -> { defence() }
                else -> { heal() }
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = Color.Black.copy(alpha = 0.3f))
            .fillMaxSize()
    ) {
        ProgressIndicator(
            progress = progress,
            modifier = Modifier.zIndex(1f)
        )

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(2f),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(0.51f)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(0.49f)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 25.dp, end = 15.dp)
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = heal
                            ),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Text(
                            text = "회복",
                            textAlign = TextAlign.Center,
                            fontFamily = DAL_MU_RI,
                            fontWeight = FontWeight.Light,
                            fontSize = 20.sp,
                            color = MongsWhite,
                            maxLines = 1,
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(0.02f)
                ) {
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .background(Color.White)
                        .width(2.dp)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(0.49f)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 25.dp, start = 15.dp)
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = defence
                            ),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Text(
                            text = "방어",
                            textAlign = TextAlign.Center,
                            fontFamily = DAL_MU_RI,
                            fontWeight = FontWeight.Light,
                            fontSize = 20.sp,
                            color = MongsWhite,
                            maxLines = 1,
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.weight(0.02f)
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(2.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(0.47f)
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 25.dp)
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = attack
                        ),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "공격",
                        textAlign = TextAlign.Center,
                        fontFamily = DAL_MU_RI,
                        fontWeight = FontWeight.Light,
                        fontSize = 20.sp,
                        color = MongsWhite,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun BattlePickDialogPreview() {
    Box{
        BattleMenuBackground()
        MatchPickDialog(
            attack = {},
            defence = {},
            heal = {},
            progress = 50f
        )
    }
}