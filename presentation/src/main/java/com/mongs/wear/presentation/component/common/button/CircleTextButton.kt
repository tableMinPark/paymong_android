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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite

@Composable
fun CircleTextButton(
    text: String,
    border: Int,
    size: Int = 54,
    onClick: () -> Unit,
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
                onClick = onClick
            )
    ) {
        Image(
            alpha = 0.6f,
            painter = painterResource(R.drawable.btn_bg_circle),
            contentDescription = null,
            modifier = Modifier.zIndex(0f)
        )
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontFamily = DAL_MU_RI,
            fontWeight = FontWeight.Light,
            fontSize = 23.sp,
            color = MongsWhite,
            maxLines = 1,
        )
        Image(
            painter = painterResource(border),
            contentDescription = null,
            modifier = Modifier.zIndex(1.2f)
        )
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun CircleTextButtonPreview() {
    CircleTextButton(
        text = "?",
        border = R.drawable.btn_border_yellow,
        onClick = {},
    )
}
