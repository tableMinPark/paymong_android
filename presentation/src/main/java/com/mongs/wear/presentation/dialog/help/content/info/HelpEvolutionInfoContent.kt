package com.mongs.wear.presentation.dialog.help.content.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.wear.compose.material.Text
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.component.common.bar.ProgressIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

@Composable
fun HelpEvolutionInfoContent() {
    val exp = remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit, Dispatchers.IO) {
        while(true) {
            delay(500)
            exp.floatValue = if (exp.floatValue + 1f > 100f) 0f else exp.floatValue + 5f
        }
    }

    Box {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .zIndex(1f)
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f)
            ) {
                Text(
                    text = "경험치를 채우면",
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    color = MongsWhite,
                    maxLines = 1,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f)
            ) {
                Text(
                    text = "몽 진화가 가능",
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    color = MongsWhite,
                    maxLines = 1,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f)
            ) {
                Text(
                    text = "다양한 활동을 통해",
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    color = MongsWhite,
                    maxLines = 1,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f)
            ) {
                Text(
                    text = "경험치를 채워보세요",
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    color = MongsWhite,
                    maxLines = 1,
                )
            }
            Spacer(modifier = Modifier.height(35.dp))
        }

        ProgressIndicator(
            progress = exp.floatValue,
            modifier = Modifier.zIndex(2f)
        )
    }
}