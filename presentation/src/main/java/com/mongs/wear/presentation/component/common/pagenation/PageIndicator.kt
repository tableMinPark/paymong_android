package com.mongs.wear.presentation.component.common.pagenation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.wear.compose.material.HorizontalPageIndicator
import androidx.wear.compose.material.PageIndicatorState
import com.mongs.wear.presentation.assets.MongsNavy
import com.mongs.wear.presentation.assets.MongsWhite

@Composable
fun PageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier.zIndex(0f)
) {
    val pageIndicatorState: PageIndicatorState = object : PageIndicatorState {
        override val pageOffset: Float
            get() = pagerState.currentPageOffsetFraction
        override val selectedPage: Int
            get() = pagerState.currentPage
        override val pageCount: Int
            get() = pagerState.pageCount
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        HorizontalPageIndicator(
            pageIndicatorState = pageIndicatorState,
            selectedColor = MongsNavy,
            unselectedColor = MongsWhite,
            indicatorSize = 6.dp,
            modifier = Modifier.padding(bottom = 5.dp)
        )
    }
}

@Composable
fun PageIndicator(
    pageIndicatorState: PageIndicatorState,
    modifier: Modifier = Modifier.zIndex(0f)
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        HorizontalPageIndicator(
            pageIndicatorState = pageIndicatorState,
            selectedColor = MongsNavy,
            unselectedColor = MongsWhite,
            indicatorSize = 6.dp,
            modifier = Modifier.padding(bottom = 5.dp)
        )
    }
}

@Preview(showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun PageIndicatorPreview() {
    PageIndicator(
        pagerState = rememberPagerState(initialPage = 2) { 5 }
    )
}