package com.mongs.wear.presentation.pages.feed.snack

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.mongs.wear.domain.management.exception.FoodCodesEmptyException
import com.mongs.wear.domain.management.usecase.FeedMongUseCase
import com.mongs.wear.domain.management.usecase.GetCurrentSlotUseCase
import com.mongs.wear.domain.management.usecase.GetSnackCodesUseCase
import com.mongs.wear.domain.management.vo.FeedItemVo
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FeedSnackPickViewModel @Inject constructor(
    private val getCurrentSlotUseCase: GetCurrentSlotUseCase,
    private val getSnackCodesUseCase: GetSnackCodesUseCase,
    private val feedMongUseCase: FeedMongUseCase,
): BaseViewModel() {

    val mongVo: LiveData<MongVo?> get() = _mongVo
    private val _mongVo = MediatorLiveData<MongVo?>(null)

    val snackVoList: LiveData<List<FeedItemVo>> get() = _snackVoList
    private val _snackVoList = MutableLiveData<List<FeedItemVo>>()

    init {
        viewModelScopeWithHandler.launch (Dispatchers.Main) {

            uiState.loadingBar = true

            _mongVo.addSource(withContext(Dispatchers.IO) { getCurrentSlotUseCase() }) { mongVo ->
                _mongVo.value = mongVo
            }

            _snackVoList.postValue(getSnackCodesUseCase())

            uiState.loadingBar = false
        }
    }

    fun buySnack(foodTypeCode: String) {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {

            uiState.loadingBar = true

            feedMongUseCase(param = FeedMongUseCase.Param(foodTypeCode = foodTypeCode))

            _snackVoList.postValue(getSnackCodesUseCase())

            effectState.eatingEffect()

            scrollPageMainPagerView()

            uiState.navMainEvent.emit(System.currentTimeMillis())
        }
    }

    val uiState: UiState = UiState()

    class UiState : BaseUiState() {
        var navMainEvent = MutableSharedFlow<Long>()
        var navFeedMenuEvent = MutableSharedFlow<Long>()
        var buyDialog by mutableStateOf(false)
        var detailDialog by mutableStateOf(false)
    }

    override fun exceptionHandler(exception: Throwable) {

        CoroutineScope(Dispatchers.IO).launch {
            when (exception) {

                is FoodCodesEmptyException -> {
                    uiState.navFeedMenuEvent.emit(System.currentTimeMillis())
                }

                else -> {
                    uiState.loadingBar = false
                    uiState.detailDialog = false
                    uiState.buyDialog = false
                }
            }
        }
    }
}