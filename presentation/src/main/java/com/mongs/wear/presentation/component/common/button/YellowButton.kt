package com.mongs.wear.presentation.component.common.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite


@Composable
fun YellowButton(
    text: String,
    onClick: () -> Unit,
    height: Int = 30,
    width: Int = 63,
    disable: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(height.dp)
            .width(width.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { if (!disable) onClick() },
            ),
    ) {
        val buttonImage = if (disable) R.drawable.btn_bg_disable else R.drawable.btn_bg_yellow

        Image(
            painter = painterResource(buttonImage),
            contentDescription = null,
            modifier = Modifier.zIndex(1.1f),
            contentScale = ContentScale.FillBounds
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.zIndex(1.2f).padding(start = 10.dp, end = 10.dp),
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontFamily = DAL_MU_RI,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
                color = MongsWhite,
                maxLines = 1,
            )
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun LongYellowButtonPreview() {
    YellowButton(text = "확인했습니다", onClick = {}, width = 100)
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun EnableYellowButtonPreview() {
    YellowButton(text = "확인", onClick = {})
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun DisableYellowButtonPreview() {
    YellowButton(text = "확인", onClick = {}, disable = true)
}