package com.mongs.wear.presentation.pages.walking

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mongs.wear.presentation.component.background.WalkingBackground

@Composable
fun WalkingView(
    walkingViewModel: WalkingViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
) {
    Box {
        WalkingBackground()
    }
}