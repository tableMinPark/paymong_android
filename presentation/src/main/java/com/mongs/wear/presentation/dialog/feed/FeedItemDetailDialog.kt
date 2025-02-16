package com.mongs.wear.presentation.dialog.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite

@Composable
fun FeedItemDetailDialog(
    onClick: () -> Unit = {},
    addWeightValue: Double = 0.0,
    addStrengthValue: Double = 0.0,
    addSatietyValue: Double = 0.0,
    addHealthyValue: Double = 0.0,
    addFatigueValue: Double = 0.0,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = Color.Black.copy(alpha = 0.85f))
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(35.dp))
            if (addHealthyValue > 0) {
                ImageDetail(
                    image = R.drawable.icon_healthy,
                    value = addHealthyValue,
                    modifier = Modifier.weight(0.2f)
                )
            }
            if (addSatietyValue > 0) {
                ImageDetail(
                    image = R.drawable.icon_satiety,
                    value = addSatietyValue,
                    modifier = Modifier.weight(0.2f)
                )
            }
            if (addStrengthValue > 0) {
                ImageDetail(
                    image = R.drawable.icon_strength,
                    value = addStrengthValue,
                    modifier = Modifier.weight(0.2f)
                )
            }
            if (addFatigueValue > 0) {
                ImageDetail(
                    image = R.drawable.icon_fatigue,
                    value = addFatigueValue,
                    modifier = Modifier.weight(0.2f)
                )
            }
            if (addWeightValue > 0) {
                TextDetail(text = "Kg", value = addWeightValue, modifier = Modifier.weight(0.2f))
            }
            Spacer(modifier = Modifier.height(35.dp))
        }
    }
}

@Composable
private fun ImageDetail(
    image: Int,
    value: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.width(100.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(0.2f),
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp),
                contentScale = ContentScale.FillBounds
            )
        }

        Text(
            text = "+ $value",
            textAlign = TextAlign.Center,
            fontFamily = DAL_MU_RI,
            fontWeight = FontWeight.Light,
            fontSize = 20.sp,
            color = MongsWhite,
            maxLines = 1,
            modifier = Modifier.weight(0.8f)
        )
    }
}


@Composable
private fun TextDetail(
    text: String,
    value: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.width(100.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(0.2f),
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontFamily = DAL_MU_RI,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp,
                color = MongsWhite,
                maxLines = 1,
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp),
            )
        }

        Text(
            text = "+ $value",
            textAlign = TextAlign.Center,
            fontFamily = DAL_MU_RI,
            fontWeight = FontWeight.Light,
            fontSize = 20.sp,
            color = MongsWhite,
            maxLines = 1,
            modifier = Modifier.weight(0.8f),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun SmallFeedItemDetailPreview() {
    FeedItemDetailDialog(
        addHealthyValue = 10.0,
        addSatietyValue = 10.0,
        addStrengthValue = 10.0,
        addFatigueValue = 10.0,
        addWeightValue = 10.0
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, showSystemUi = true, device = Devices.WEAR_OS_LARGE_ROUND)
@Composable
private fun LargeFeedItemDetailPreview() {
    FeedItemDetailDialog(
        addHealthyValue = 10.0,
        addSatietyValue = 10.0,
        addStrengthValue = 10.0,
        addFatigueValue = 10.0,
        addWeightValue = 10.0
    )
}