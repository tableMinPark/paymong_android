package com.mongs.wear.presentation.global.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mongs.wear.core.exception.global.PresentationException
import com.mongs.wear.core.exception.global.UseCaseException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    companion object {

        private const val TAG = "BaseViewModel"

        private val _errorEvent = MutableSharedFlow<String>()
        val errorEvent = _errorEvent.asSharedFlow()

        private val _successEvent = MutableSharedFlow<String>()
        val successEvent = _successEvent.asSharedFlow()

        private val _animatePageScrollMainPagerViewEvent = MutableSharedFlow<Long>()
        val animatePageScrollMainPagerViewEvent = _animatePageScrollMainPagerViewEvent.asSharedFlow()

        suspend fun toastEvent(message: String) {
            _successEvent.emit(message)
        }

        suspend fun scrollPageMainPagerView() {
            _animatePageScrollMainPagerViewEvent.emit(System.currentTimeMillis())
        }

        val effectState = BaseEffectState()
    }

    /**
     * ViewModel Exception Handler
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        CoroutineScope(Dispatchers.IO).launch {
            if (exception is UseCaseException) {
                Log.e(TAG, "[ViewModel Exception] ${exception.javaClass.simpleName} ${exception.message} ===> ${exception.result}")

                // 알림 표출
                if (exception.code.isMessageShow()) {
                    _errorEvent.emit(exception.message)
                }
            } else if (exception is PresentationException) {
                Log.e(TAG, "[ViewModel Exception] ${exception.javaClass.simpleName} ${exception.message} ===> ${exception.result}")

                // 알림 표출
                if (exception.code.isMessageShow()) {
                    _errorEvent.emit(exception.message)
                }
            } else {
                Log.e(TAG, "[ViewModel Exception] ${exception.javaClass.name} ${exception.message ?: ""}")
            }

            // 자식 클래스 exception handler 실행
            exceptionHandler(exception = exception)
        }
    }

    /**
     * ViewModel Exception Handler Override
     * ViewModel 자식 클래스에서 Exception Handler 를 재정의 하여 사용
     */
    abstract suspend fun exceptionHandler(exception: Throwable)

    protected val viewModelScopeWithHandler = CoroutineScope(
        viewModelScope.coroutineContext + exceptionHandler
    )

    open class BaseUiState {
        var loadingBar by mutableStateOf(true)
    }

    open class BaseEffectState {

        companion object {
            private const val EFFECT_DELAY = 2000L
        }

        var isHappy by mutableStateOf(false)
        var isEating by mutableStateOf(false)
        var isPoopCleaning by mutableStateOf(false)

        fun happyEffect() {
            CoroutineScope(Dispatchers.IO).launch {
                isHappy = true
                delay(EFFECT_DELAY)
                isHappy = false
            }
        }

        fun poopCleaningEffect() {
            CoroutineScope(Dispatchers.IO).launch {
                isPoopCleaning = true
                delay(EFFECT_DELAY)
                isPoopCleaning = false
            }
        }

        fun eatingEffect() {
            CoroutineScope(Dispatchers.IO).launch {
                isEating = true
                delay(EFFECT_DELAY)
                isEating = false
            }
        }
    }
}