package com.mongs.wear.presentation.pages.collection.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mongs.wear.core.exception.usecase.GetMapCollectionsUseCaseException
import com.mongs.wear.core.exception.usecase.SetBackgroundMapCodeUseCaseException
import com.mongs.wear.domain.collection.usecase.GetMapCollectionsUseCase
import com.mongs.wear.domain.collection.vo.MapCollectionVo
import com.mongs.wear.domain.device.usecase.SetBackgroundMapCodeUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CollectionMapPickViewModel @Inject constructor(
    private val getMapCollectionsUseCase: GetMapCollectionsUseCase,
    private val setBackgroundMapCodeUseCase: SetBackgroundMapCodeUseCase,
): BaseViewModel() {

    val mapCollectionVoList: LiveData<List<MapCollectionVo>> get() = _mapCollectionVoList
    private val _mapCollectionVoList = MutableLiveData<List<MapCollectionVo>>()

    init {
        viewModelScopeWithHandler.launch (Dispatchers.Main) {

            uiState.loadingBar = true

            _mapCollectionVoList.postValue(withContext(Dispatchers.IO) {
                getMapCollectionsUseCase()
            })

            uiState.loadingBar = false
        }
    }

    /**
     * 배경 설정
     */
    fun setBackground(mapCode: String) {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {

            uiState.loadingBar = true

            setBackgroundMapCodeUseCase(
                SetBackgroundMapCodeUseCase.Param(
                    mapTypeCode = mapCode
                )
            )

            uiState.detailDialog = false
            uiState.loadingBar = false
        }
    }

    val uiState: UiState = UiState()

    class UiState : BaseUiState() {
        var navCollectionMenu by mutableStateOf(false)
        var detailDialog by mutableStateOf(false)
    }

    override suspend fun exceptionHandler(exception: Throwable) {
        when(exception) {
            is GetMapCollectionsUseCaseException -> {
                uiState.loadingBar = false
                uiState.navCollectionMenu = true
            }

            is SetBackgroundMapCodeUseCaseException -> {
                uiState.loadingBar = false
                uiState.detailDialog = false
            }

            else -> {
                uiState.loadingBar = false
                uiState.detailDialog = false
            }
        }
    }
}