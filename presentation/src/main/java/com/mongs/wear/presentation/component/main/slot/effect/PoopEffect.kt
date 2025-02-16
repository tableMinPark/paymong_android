package com.mongs.wear.presentation.component.main.slot.effect

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mongs.wear.presentation.R

private val poopPadding = arrayOf(
    PaddingValues(end = 80.dp,bottom = 26.dp),
    PaddingValues(start = 76.dp,bottom = 28.dp),
    PaddingValues(end = 60.dp,bottom = 18.dp),
    PaddingValues(start = 54.dp, bottom = 16.dp)
)

@Composable
fun PoopEffect(
    poopCount: Int = 0,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxSize(),
    ) {
        for (count in 1..poopCount) {
            Image(
                modifier = Modifier
                    .padding(poopPadding[count - 1])
                    .size(25.dp),
                painter = painterResource(R.drawable.icon_poop),
                contentDescription = null
            )
        }
    }
}