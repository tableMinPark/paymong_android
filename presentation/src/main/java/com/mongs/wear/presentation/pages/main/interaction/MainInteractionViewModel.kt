package com.mongs.wear.presentation.pages.main.interaction

import com.mongs.wear.core.exception.usecase.PoopCleanMongUseCaseException
import com.mongs.wear.core.exception.usecase.SleepingMongUseCaseException
import com.mongs.wear.domain.management.usecase.PoopCleanMongUseCase
import com.mongs.wear.domain.management.usecase.SleepingMongUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainInteractionViewModel @Inject constructor(
    private val sleepingMongUseCase: SleepingMongUseCase,
    private val poopCleanMongUseCase: PoopCleanMongUseCase,
): BaseViewModel() {

    /**
     * 몽 수면/기상
     */
    fun sleeping(mongId: Long) {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {
            sleepingMongUseCase(
                SleepingMongUseCase.Param(
                    mongId = mongId
                )
            )

            scrollPageMainPagerView()
        }
    }

    /**
     * 몽 청소
     */
    fun poopClean(mongId: Long) {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {

            poopCleanMongUseCase(
                PoopCleanMongUseCase.Param(
                    mongId = mongId
                )
            )

            scrollPageMainPagerView()

            effectState.poopCleaningEffect()
        }
    }

    val uiState: UiState = UiState()

    class UiState : BaseUiState() {}

    override suspend fun exceptionHandler(exception: Throwable) {
        when(exception) {
            is SleepingMongUseCaseException -> {
                uiState.loadingBar = false
            }

            is PoopCleanMongUseCaseException -> {
                uiState.loadingBar = false
            }

            else -> {
                uiState.loadingBar = false
            }
        }
    }
}