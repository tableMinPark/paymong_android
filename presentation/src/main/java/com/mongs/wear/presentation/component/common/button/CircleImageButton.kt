package com.mongs.wear.presentation.component.common.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.wear.compose.material.MaterialTheme
import coil.ImageLoader
import coil.decode.ImageDecoderDecoder
import com.mongs.wear.presentation.R

@Composable
fun CircleImageButton(
    icon: Int,
    border: Int,
    size: Int = 54,
    iconSize: Int = size / 2,
    onClick: () -> Unit,
    disable: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size.dp)
            .background(
                color = Color.Transparent,
                shape = MaterialTheme.shapes.large
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    if (!disable) {
                        onClick()
                    }
                }
            )
    ) {
        Image(
            alpha = 0.55f,
            painter = painterResource(R.drawable.interaction_bnt),
            contentDescription = null,
            modifier = Modifier.zIndex(0f)
        )

        Image(
            alpha = if (disable) 0.4f else 1f,
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .size(iconSize.dp)
                .zIndex(if (disable) -1f else 1f)
        )

        if (disable) {
            Image(
                painter = painterResource(R.drawable.locker),
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize.dp)
                    .zIndex(2f)
            )
        }

        Image(
            painter = painterResource(border),
            contentDescription = null,
            modifier = Modifier.zIndex(3f)
        )
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun LongBlueButtonPreview() {
    CircleImageButton(
        icon = R.drawable.basketball,
        border = R.drawable.interaction_bnt_yellow,
        disable = true,
        onClick = {},
    )
}
