package com.mongs.wear.presentation.dialog.help

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import com.mongs.wear.presentation.component.common.pagenation.PageIndicator
import com.mongs.wear.presentation.dialog.help.content.common.HelpCancelContent
import com.mongs.wear.presentation.dialog.help.content.point.HelpAddPayPointContent
import com.mongs.wear.presentation.dialog.help.content.point.HelpAddStarPointContent
import com.mongs.wear.presentation.dialog.help.content.point.HelpWarningPointContent

@Composable
fun HelpPointDialog(
    cancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(initialPage = 0) { 4 }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = Color.Black.copy(alpha = 0.85f))
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = cancel,
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
        ) {
            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> {
                        HelpAddPayPointContent()
                    }

                    1 -> {
                        HelpAddStarPointContent()
                    }

                    2 -> {
                        HelpWarningPointContent()
                    }

                    3 -> {
                        HelpCancelContent(cancel = cancel)
                    }
                }
            }
        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
        ) {
            PageIndicator(pagerState = pagerState)
        }
    }
}
