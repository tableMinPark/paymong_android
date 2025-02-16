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
import com.mongs.wear.presentation.component.help.content.common.HelpCancelContent
import com.mongs.wear.presentation.component.help.content.mong.HelpMongContent_0
import com.mongs.wear.presentation.component.help.content.mong.HelpMongContent_1
import com.mongs.wear.presentation.component.help.content.mong.HelpMongContent_2
import com.mongs.wear.presentation.component.help.content.mong.HelpMongContent_3
import com.mongs.wear.presentation.component.help.content.mong.HelpMongContent_4
import com.mongs.wear.presentation.component.help.content.mong.HelpMongContent_5
import com.mongs.wear.presentation.component.help.content.mong.HelpMongContent_6
import com.mongs.wear.presentation.component.help.content.mong.HelpMongContent_7
import com.mongs.wear.presentation.component.help.content.mong.HelpMongContent_8
import com.mongs.wear.presentation.component.help.content.mong.HelpMongContent_9

@Composable
fun HelpMongDialog(
    cancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(initialPage = 0) { 11 }

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
                        HelpMongContent_0()
                    }

                    1 -> {
                        HelpMongContent_1()
                    }

                    2 -> {
                        HelpMongContent_2()
                    }

                    3 -> {
                        HelpMongContent_3()
                    }

                    4 -> {
                        HelpMongContent_4()
                    }

                    5 -> {
                        HelpMongContent_5()
                    }

                    6 -> {
                        HelpMongContent_6()
                    }

                    7 -> {
                        HelpMongContent_7()
                    }

                    8 -> {
                        HelpMongContent_8()
                    }

                    9 -> {
                        HelpMongContent_9()
                    }

                    10 -> {
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