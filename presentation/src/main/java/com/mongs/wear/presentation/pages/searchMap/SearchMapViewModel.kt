package com.mongs.wear.presentation.pages.searchMap

import android.util.Log
import com.mongs.wear.domain.collection.usecase.CreateMapCollectionUseCase
import com.mongs.wear.presentation.global.manager.LocationSensorManager
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMapViewModel @Inject constructor(
    private val createMapCollectionUseCase: CreateMapCollectionUseCase,
    private val locationSensorManager: LocationSensorManager,
): BaseViewModel() {

    companion object {
        private const val TAG = "SearchMapViewModel"
    }

    init {
        search()
    }

    /**
     * 맵 탐색
     */
    fun search() {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {
            locationSensorManager.getLocation()?.let { locationVo ->

                Log.i(TAG, "$locationVo")

                createMapCollectionUseCase(
                    CreateMapCollectionUseCase.Param(
                        latitude = locationVo.latitude,
                        longitude = locationVo.longitude,
                    )
                )
            }
        }
    }

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