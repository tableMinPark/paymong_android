package com.mongs.wear.presentation.component.common.bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import com.mongs.wear.presentation.R

@Composable
fun LoadingBar(
    height: Int = 40,
    width: Int = 40,
) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components { add(ImageDecoderDecoder.Factory()) }
        .build()
    val loading = R.drawable.icon_loading

    Image(
        painter = rememberAsyncImagePainter(model = loading, imageLoader = imageLoader),
        contentDescription = null,
        modifier = Modifier
            .height(height.dp)
            .width(width.dp)
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun LoadingBarPreview() {
    LoadingBar()
}