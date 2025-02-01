package com.mongs.wear.domain.auth.usecase

import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.auth.exception.JoinException
import com.mongs.wear.domain.auth.exception.NotExistsEmailException
import com.mongs.wear.domain.auth.exception.NotExistsGoogleAccountIdException
import com.mongs.wear.domain.auth.exception.NotExistsNameException
import com.mongs.wear.domain.auth.repository.AuthRepository
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * 회원 가입 UseCase
 */
class JoinUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseParamUseCase<JoinUseCase.Param, Unit>() {

    override suspend fun execute(param: Param) {

        withContext(Dispatchers.IO) {

            if (param.email.isNullOrEmpty()) throw NotExistsEmailException()

            if (param.name.isNullOrEmpty()) throw NotExistsNameException()

            if (param.googleAccountId.isNullOrEmpty()) throw NotExistsGoogleAccountIdException()

            authRepository.join(
                email = param.email,
                name = param.name,
                googleAccountId = param.googleAccountId,
            )
        }
    }

    data class Param(

        val googleAccountId: String?,

        val email: String?,

        val name: String?
    )

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {
            else -> throw JoinException()
        }
    }
}
