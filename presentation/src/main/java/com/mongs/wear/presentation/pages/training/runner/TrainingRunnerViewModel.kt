package com.mongs.wear.presentation.pages.training.runner

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.mongs.wear.core.enums.TrainingCode
import com.mongs.wear.domain.management.usecase.GetCurrentSlotUseCase
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.domain.training.exception.GetTrainingPayPointException
import com.mongs.wear.domain.training.usecase.GetTrainingPayPointUseCase
import com.mongs.wear.domain.training.usecase.TrainingMongUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TrainingRunnerViewModel @Inject constructor(
    private val getTrainingPayPointUseCase: GetTrainingPayPointUseCase,
    private val getCurrentSlotUseCase: GetCurrentSlotUseCase,
    private val trainingMongUseCase: TrainingMongUseCase,
) : BaseViewModel() {

    companion object {
        private const val DEFAULT_Y = 5f

        private const val PLAYER_HEIGHT = 50
        private const val PLAYER_WIDTH = 50
    }

    val runnerEngine = RunnerEngine(defaultY = DEFAULT_Y)

    private val _mongVo = MediatorLiveData<MongVo?>(null)
    val mongVo: LiveData<MongVo?> get() = _mongVo

    private val _trainingPayPoint = MediatorLiveData<Int>(0)
    val trainingPayPoint: LiveData<Int> get() = _trainingPayPoint

    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {

            uiState.loadingBar = true

            _trainingPayPoint.postValue(
                getTrainingPayPointUseCase(
                    GetTrainingPayPointUseCase.Param(
                        trainingCode = TrainingCode.RUNNER,
                    )
                )
            )

            _mongVo.addSource(withContext(Dispatchers.IO) { getCurrentSlotUseCase() }) { mongVo ->
                _mongVo.value = mongVo
            }

            uiState.loadingBar = false
        }
    }

    fun runnerStart() {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {
            runnerEngine.endEvent.collect {
                uiState.trainingOverDialog = true
            }
        }

        viewModelScopeWithHandler.launch(Dispatchers.IO) {

            uiState.trainingStartDialog = false
            uiState.trainingOverDialog = false

            runnerEngine.start(
                height = PLAYER_HEIGHT,
                width = PLAYER_WIDTH,
            )
        }
    }

    fun runnerEnd(mongId: Long, score: Int) {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {
            runnerEngine.end()

            trainingMongUseCase(
                TrainingMongUseCase.Param(
                    mongId =  mongId,
                    score = score,
                    trainingCode = TrainingCode.RUNNER,
                )
            )

            uiState.navMainEvent.emit(System.currentTimeMillis())
        }
    }

    val uiState = UiState()

    class UiState : BaseUiState() {
        var navMainEvent = MutableSharedFlow<Long>()
        var trainingStartDialog by mutableStateOf(true)
        var trainingOverDialog by mutableStateOf(false)
    }

    override fun exceptionHandler(exception: Throwable) {

        CoroutineScope(Dispatchers.IO).launch {
            when (exception) {

                is GetTrainingPayPointException -> {
                    uiState.navMainEvent.emit(System.currentTimeMillis())
                }

                else -> {
                    uiState.loadingBar = false
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        runnerEngine.end()
    }
}