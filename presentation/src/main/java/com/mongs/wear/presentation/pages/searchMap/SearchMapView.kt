package com.mongs.wear.presentation.pages.searchMap

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mongs.wear.core.errors.PresentationErrorCode
import com.mongs.wear.presentation.component.background.SearchMapBackground

@Composable
fun SearchMapView(
    searchMapViewModel: SearchMapViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
) {
    Box {
        SearchMapBackground()

        LaunchedEffect(Unit) {
            Toast.makeText(
                context,
                "맵 탐색 ${PresentationErrorCode.PRESENTATION_UPDATE_SOON.getMessage()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}