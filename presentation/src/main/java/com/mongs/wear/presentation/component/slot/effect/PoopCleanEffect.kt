package com.mongs.wear.presentation.component.slot.effect

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import com.mongs.wear.presentation.R

@Composable
fun PoopCleanEffect(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components { add(ImageDecoderDecoder.Factory()) }
        .build()

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .fillMaxSize(),
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = R.drawable.effect_vacuum,
                imageLoader = imageLoader
            ),
            contentDescription = "PoopCleanEffect",
            modifier = Modifier
                .size(140.dp)
                .padding(bottom = 20.dp),
        )
    }
}
