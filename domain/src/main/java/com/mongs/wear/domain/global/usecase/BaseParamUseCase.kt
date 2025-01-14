package com.mongs.wear.domain.global.usecase

import android.util.Log
import com.mongs.wear.core.errors.DomainErrorCode
import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.core.exception.UseCaseException

abstract class BaseParamUseCase<P, R> {

    companion object {
        private const val TAG = "BaseParamUseCase"
    }

    abstract suspend fun execute(param: P): R

    suspend operator fun invoke(param: P): R {

        Log.i(TAG, "[UseCase] ${this.javaClass.name} $param")

        return try {
            // 메서드 실행
            this.execute(param = param)
        } catch (exception: Exception) {

            when (exception) {
                is UseCaseException -> throw exception

                is ErrorException -> {
                    Log.i(TAG, "[Exception] ${exception.javaClass.name} ${exception.message} ${exception.result}")

                    handleException(exception = exception)

                    // handlerException 에서 throw 하지 않을 시에 기본 예외 발생
                    throw UseCaseException(
                        code = DomainErrorCode.DOMAIN_GLOBAL_DATA_ERROR,
                        message = exception.message
                    )
                }

                else -> {
                    Log.i(TAG, "[Exception] ${exception.javaClass.name} ${exception.message ?: ""}")

                    throw UseCaseException(
                        code = DomainErrorCode.DOMAIN_GLOBAL_UNKNOWN_ERROR,
                        message = exception.message ?: ""
                    )
                }
            }
        }
    }

    protected open fun handleException(exception: ErrorException) {}
}

