package com.mongs.wear.presentation.component.background

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.mongs.wear.presentation.R

@Composable
fun TrainingNestedBackground(
    isMoving: Boolean = false,
    modifier: Modifier = Modifier.zIndex(0f)
) {
    if (!isMoving) {
        val background = R.drawable.bg_training

        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize(),
        ) {
            Image(
                painter = painterResource(background),
                contentDescription = "TrainingNestedBackground",
                contentScale = ContentScale.Crop
            )
        }
    } else {

        val backgrounds = arrayOf(
            ImageBitmap.imageResource(R.drawable.bg_training),
            ImageBitmap.imageResource(R.drawable.bg_training_mirror),
            ImageBitmap.imageResource(R.drawable.bg_training),
        )

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

            val infiniteTransition = rememberInfiniteTransition(label = "TrainingNestedBackgroundTransition")
            val offsetX by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = constraints.maxWidth.toFloat() * -(backgrounds.size - 1),       // 배경 수 - 1 = 2
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = constraints.maxWidth * backgrounds.size * 2,           // 배경 속도 보정
                        easing = LinearEasing,
                    ),
                    repeatMode = RepeatMode.Restart
                ), label = "TrainingNestedBackgroundOffset"
            )

            Canvas(modifier = Modifier.fillMaxSize()) {

                val canvasWidth = size.width
                val canvasHeight = size.height

                backgrounds.forEachIndexed { index, background ->
                    drawBackground(
                        background,
                        offsetX + canvasWidth * index,
                        background.width.toFloat(),
                        background.height.toFloat(),
                        canvasWidth,
                        canvasHeight,
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawBackground(image: ImageBitmap, offsetX: Float, imageWidth: Float, imageHeight: Float, canvasWidth: Float, canvasHeight: Float) {

    val painter =  android.graphics.Paint()

    drawIntoCanvas { canvas ->
        canvas.nativeCanvas.drawBitmap(
            image.asAndroidBitmap(),
            null,
            android.graphics.RectF(
                offsetX,
                0f,
                (canvasWidth / imageWidth * imageWidth) + offsetX + 0.8f,
                (canvasHeight / imageHeight * imageHeight),
            ),
            painter,
        )
    }
}

@Preview(showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun TrainingNestedBackgroundPreview() {
    TrainingNestedBackground()
}