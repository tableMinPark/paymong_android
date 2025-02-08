package com.mongs.wear.domain.auth.usecase

import com.mongs.wear.core.exception.data.JoinException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.JoinUseCaseException
import com.mongs.wear.domain.auth.repository.AuthRepository
import com.mongs.wear.core.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * 회원 가입 UseCase
 * @throws JoinException 회원 가입 실패 예외 클래스
 */
class JoinUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseParamUseCase<JoinUseCase.Param, Unit>() {

    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            authRepository.join(
                email = param.email,
                name = param.name,
                googleAccountId = param.googleAccountId,
            )
        }
    }

    data class Param(

        val googleAccountId: String,

        val email: String,

        val name: String,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is JoinException -> throw JoinUseCaseException()

            else -> throw JoinUseCaseException()
        }
    }
}
