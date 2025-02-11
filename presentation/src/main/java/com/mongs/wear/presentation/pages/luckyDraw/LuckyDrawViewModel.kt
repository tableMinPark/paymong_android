package com.mongs.wear.presentation.pages.luckyDraw

import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LuckyDrawViewModel @Inject constructor(
): BaseViewModel() {

    val uiState: UiState = UiState()

    class UiState : BaseUiState() {}

    override suspend fun exceptionHandler(exception: Throwable) {
        when(exception) {
            else -> {
                uiState.loadingBar = false
            }
        }
    }
}