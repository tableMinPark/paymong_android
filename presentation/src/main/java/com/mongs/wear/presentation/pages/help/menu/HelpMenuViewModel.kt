package com.mongs.wear.presentation.pages.help.menu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpMenuViewModel @Inject constructor() : BaseViewModel() {

    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {
            uiState.loadingBar = false
        }
    }

    val uiState = UiState()

    class UiState : BaseUiState() {
        var helpInfoDialog by mutableStateOf(false)
        var helpPointDialog by mutableStateOf(false)
        var helpMongDialog by mutableStateOf(false)
        var helpSlotDialog by mutableStateOf(false)
        var helpBattleDialog by mutableStateOf(false)
        var helpTrainingDialog by mutableStateOf(false)
    }

    override suspend fun exceptionHandler(exception: Throwable) {}
}