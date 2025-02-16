package com.mongs.wear.presentation.pages.luckyDraw

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mongs.wear.presentation.component.background.LuckyDrawBackground

@Composable
fun LuckyDrawView(
    luckyDrawViewModel: LuckyDrawViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
) {
    Box {
        LuckyDrawBackground()
    }
}