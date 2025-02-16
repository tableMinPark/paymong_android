package com.mongs.wear.presentation.pages.exchange.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.mongs.wear.core.exception.usecase.GetCurrentSlotUseCaseException
import com.mongs.wear.domain.management.usecase.GetCurrentSlotUseCase
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.global.manager.BillingManager
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExchangeMenuViewModel @Inject constructor(
    private val billingManager: BillingManager,
    private val getCurrentSlotUseCase: GetCurrentSlotUseCase,
) : BaseViewModel() {

    private val _mongVo = MediatorLiveData<MongVo?>(null)
    val mongVo: LiveData<MongVo?> get() = _mongVo

    private val _isBillingDevice = MediatorLiveData<Boolean>()
    val isBillingDevice: LiveData<Boolean> get() = _isBillingDevice

    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {

            uiState.loadingBar = true

            _mongVo.addSource(withContext(Dispatchers.IO) { getCurrentSlotUseCase() }) {
                it?.let { mongVo ->
                    _mongVo.value = mongVo
                }
            }

            _isBillingDevice.postValue(billingManager.verifyBilling())

            uiState.loadingBar = false
        }
    }

    val uiState = UiState()

    class UiState : BaseUiState() {}

    override suspend fun exceptionHandler(exception: Throwable) {
        when(exception) {
            is GetCurrentSlotUseCaseException -> {
                uiState.loadingBar = false
            }

            else -> {
                uiState.loadingBar = false
            }
        }
    }
}