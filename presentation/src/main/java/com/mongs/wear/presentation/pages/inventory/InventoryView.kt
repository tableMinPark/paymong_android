package com.mongs.wear.presentation.pages.inventory

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mongs.wear.core.errors.PresentationErrorCode
import com.mongs.wear.presentation.component.background.InventoryBackground

@Composable
fun InventoryView(
    inventoryViewModel: InventoryViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
) {
    Box {
        InventoryBackground()

        LaunchedEffect(Unit) {
            Toast.makeText(
                context,
                "인벤 ${PresentationErrorCode.PRESENTATION_UPDATE_SOON.getMessage()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}