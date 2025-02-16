package com.mongs.wear.presentation.component.common.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.component.common.button.CircleImageButton

@Composable
fun IconChip(
    icon: Int,
    border: Int,
    fontColor: Color,
    backgroundColor: Color,
    label: String,
    secondaryLabel: String = "",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Chip(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        icon = {
            CircleImageButton(
                icon = icon,
                border = border,
                size = 37,
                onClick = {},
            )
        },
        colors = ChipDefaults.chipColors(
            contentColor = fontColor,
            backgroundColor = backgroundColor.copy(alpha = 0.3f)
        ),
        label = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = label,
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                    color = fontColor,
                    maxLines = 1,
                )
            }
        },
        secondaryLabel = {
            if (secondaryLabel.isNotBlank()) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
                ) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = secondaryLabel,
                        textAlign = TextAlign.Center,
                        fontFamily = DAL_MU_RI,
                        fontWeight = FontWeight.Light,
                        fontSize = 10.sp,
                        color = fontColor,
                        maxLines = 1,
                    )
                }
            }
        }
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun StarPointPreview() {
    IconChip(
        icon = R.drawable.icon_satiety,
        border = R.drawable.btn_border_yellow,
        fontColor = MongsWhite,
        label = "테스트",
        secondaryLabel = "",
        backgroundColor = Color.Black,
    )
}