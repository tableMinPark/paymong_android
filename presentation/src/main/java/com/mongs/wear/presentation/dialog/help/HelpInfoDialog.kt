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
import com.mongs.wear.presentation.component.help.content.info.*
import com.mongs.wear.presentation.component.help.content.common.*

@Composable
fun HelpInfoDialog(
    cancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(initialPage = 0) { 9 }

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
                        HelpInfoContent_0()
                    }

                    1 -> {
                        HelpInfoContent_1()
                    }

                    2 -> {
                        HelpInfoContent_2()
                    }

                    3 -> {
                        HelpInfoContent_3()
                    }

                    4 -> {
                        HelpInfoContent_4()
                    }

                    5 -> {
                        HelpInfoContent_5()
                    }

                    6 -> {
                        HelpInfoContent_6()
                    }

                    7 -> {
                        HelpInfoContent_7()
                    }

                    8 -> {
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
