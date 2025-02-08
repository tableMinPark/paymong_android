package com.mongs.wear.domain.feedback.usecase

import com.mongs.wear.core.exception.data.CreateFeedbackException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.CreateFeedbackUseCaseException
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.domain.feedback.repository.FeedbackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateFeedbackUseCase @Inject constructor(
    private val feedbackRepository: FeedbackRepository,
) : BaseParamUseCase<CreateFeedbackUseCase.Param, Unit>() {

    /**
     * 오류 신고 등록 UseCase
     * @throws CreateFeedbackException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            feedbackRepository.createFeedback(
                title = param.title,
                content = param.content,
            )
        }
    }

    data class Param(

        val title: String,

        val content: String,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is CreateFeedbackException -> throw CreateFeedbackUseCaseException()

            else -> throw CreateFeedbackUseCaseException()
        }
    }
}