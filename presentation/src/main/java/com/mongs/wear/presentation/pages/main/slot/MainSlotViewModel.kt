package com.mongs.wear.presentation.pages.main.slot

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mongs.wear.core.exception.usecase.EvolutionMongUseCaseException
import com.mongs.wear.core.exception.usecase.GraduateCheckUseCaseException
import com.mongs.wear.core.exception.usecase.StrokeMongUseCaseException
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

    /**
     * 몽 쓰다 듬기
     */
    fun stroke(mongId: Long) {

        if (effectState.isHappy) return

        viewModelScopeWithHandler.launch (Dispatchers.IO) {
            strokeMongUseCase(
                StrokeMongUseCase.Param(
                    mongId = mongId
                )
            )

            effectState.happyEffect()
        }
    }

    /**
     * 몽 진화
     */
    fun evolution(mongId: Long) {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {

            evolutionMongUseCase(
                EvolutionMongUseCase.Param(
                    mongId = mongId
                )
            )
        }
    }

    /**
     * 몽 졸업 체크
     */
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

    override suspend fun exceptionHandler(exception: Throwable) {
        when(exception) {
            is StrokeMongUseCaseException -> {
                uiState.loadingBar = false
            }

            is EvolutionMongUseCaseException -> {
                uiState.loadingBar = false
            }

            is GraduateCheckUseCaseException -> {
            }

            else -> {
                uiState.loadingBar = false
            }
        }
    }
}