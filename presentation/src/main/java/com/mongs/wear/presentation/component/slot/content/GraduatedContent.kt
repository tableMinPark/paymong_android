package com.mongs.wear.presentation.component.slot.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.wear.compose.material.Text
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongResourceCode
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.component.common.charactor.Mong

@Composable
fun GraduatedContent(
    mongVo: MongVo,
    isPageChanging: Boolean,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.6f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .zIndex(1f)
                .fillMaxSize()
        ) {
            Mong(
                mong = MongResourceCode.valueOf(mongVo.mongTypeCode),
                modifier = Modifier.padding(bottom = 25.dp)
            )
        }

        if (!isPageChanging) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .zIndex(2f)
                    .background(color = Color.Black.copy(alpha = 0.6f))
                    .fillMaxSize()
            ) {
                Text(
                    text = "졸업한 몽입니다\n\n슬롯을 변경해주세요",
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                    color = MongsWhite,
                )
            }
        }
    }
}