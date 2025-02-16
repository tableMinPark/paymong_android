package com.mongs.wear.presentation.component.common.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mongs.wear.presentation.R

@Composable
fun LeftButton(
    height: Int = 35,
    width: Int = 18,
    onClick: () -> Unit,
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
                onClick = onClick,
            ),
    ) {
        val buttonImage = R.drawable.btn_icon_left

        Image(
            painter = painterResource(buttonImage),
            contentDescription = "LeftButton",
            modifier = Modifier.zIndex(1.1f),
            contentScale = ContentScale.FillBounds
        )
    }
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun LeftButtonPreview() {
    LeftButton(
        onClick = {},
    )
}
