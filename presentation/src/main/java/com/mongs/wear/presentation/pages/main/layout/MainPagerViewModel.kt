package com.mongs.wear.presentation.pages.main.layout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.mongs.wear.domain.device.exception.GetBackgroundMapCodeException
import com.mongs.wear.domain.device.usecase.GetBackgroundMapCodeUseCase
import com.mongs.wear.domain.management.exception.GetCurrentSlotException
import com.mongs.wear.domain.management.usecase.GetCurrentSlotUseCase
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainPagerViewModel @Inject constructor(
    private val getCurrentSlotUseCase: GetCurrentSlotUseCase,
    private val getBackgroundMapCodeUseCase: GetBackgroundMapCodeUseCase,
): BaseViewModel() {

    private val _mongVo = MediatorLiveData<MongVo?>(null)
    val mongVo: LiveData<MongVo?> get() = _mongVo

    private val _backgroundMapCode = MediatorLiveData<String>()
    val backgroundMapCode: LiveData<String> get() = _backgroundMapCode

    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {

            uiState.loadingBar = true

            _mongVo.addSource(withContext(Dispatchers.IO) { getCurrentSlotUseCase() }) { mongVo ->
                _mongVo.value = mongVo
            }

            _backgroundMapCode.addSource(withContext(Dispatchers.IO) { getBackgroundMapCodeUseCase() }) { backgroundMapCode ->
                _backgroundMapCode.value = backgroundMapCode
            }

            uiState.loadingBar = false
        }
    }

    val uiState = UiState()

    class UiState : BaseUiState() {}

    override fun exceptionHandler(exception: Throwable) {

        when(exception) {
            is GetCurrentSlotException -> {
                uiState.loadingBar = false
            }

            is GetBackgroundMapCodeException -> {
                uiState.loadingBar = false
            }

            else -> {
                uiState.loadingBar = false
            }
        }
    }
}