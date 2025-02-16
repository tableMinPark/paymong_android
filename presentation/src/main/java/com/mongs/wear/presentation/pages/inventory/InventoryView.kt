package com.mongs.wear.presentation.pages.inventory

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mongs.wear.presentation.component.background.InventoryBackground

@Composable
fun InventoryView(
    inventoryViewModel: InventoryViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
) {
    Box {
        InventoryBackground()
    }
}