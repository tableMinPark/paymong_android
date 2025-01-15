package com.mongs.wear.presentation.pages.main.slot

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mongs.wear.domain.management.exception.EvolutionMongException
import com.mongs.wear.domain.management.exception.GraduateCheckException
import com.mongs.wear.domain.management.exception.StrokeMongException
import com.mongs.wear.domain.management.usecase.EvolutionMongUseCase
import com.mongs.wear.domain.management.usecase.GraduateCheckMongUseCase
import com.mongs.wear.domain.management.usecase.StrokeMongUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainSlotViewModel @Inject constructor(
    private val strokeMongUseCase: StrokeMongUseCase,
    private val evolutionMongUseCase: EvolutionMongUseCase,
    private val graduateCheckMongUseCase: GraduateCheckMongUseCase,
): BaseViewModel() {

    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {
            uiState.loadingBar = false
        }
    }

    fun stroke(mongId: Long) {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {
            strokeMongUseCase(
                StrokeMongUseCase.Param(
                    mongId = mongId
                )
            )

            effectState.happyEffect()
        }
    }

    fun evolution(mongId: Long) {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {

            evolutionMongUseCase(
                EvolutionMongUseCase.Param(
                    mongId = mongId
                )
            )
        }
    }

    fun graduationReady(mongId: Long) {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {
            graduateCheckMongUseCase(
                GraduateCheckMongUseCase.Param(
                    mongId = mongId
                )
            )
        }
    }

    val uiState: UiState = UiState()

    class UiState : BaseUiState() {
        var isEvolution by mutableStateOf(false)
    }

    override fun exceptionHandler(exception: Throwable) {

        when(exception) {
            is StrokeMongException -> {
                uiState.loadingBar = false
            }

            is EvolutionMongException -> {
                uiState.loadingBar = false
            }

            is GraduateCheckException -> {
            }

            else -> {
                uiState.loadingBar = false
            }
        }
    }
}