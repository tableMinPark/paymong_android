package com.mongs.wear.presentation.component.common.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mongs.wear.presentation.assets.MongsRed

@Composable
fun HpBar(
    hp: Float,
    height: Int = 20,
    width: Int = 65,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .height(height.dp)
            .width(width.dp)
            .clip(CircleShape)
            .background(Color.White),
    ) {
        Row(
            modifier = Modifier
                .height(height.dp)
                .fillMaxWidth(fraction = hp / 100f)
                .background(color = MongsRed)
        ) {}
    }
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun PayPointPreview() {
    HpBar(hp = 100f)
}