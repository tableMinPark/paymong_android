package com.mongs.wear.domain.usecase.battle

import androidx.lifecycle.LiveData
import com.mongs.wear.domain.code.FeedbackCode
import com.mongs.wear.domain.exception.RepositoryException
import com.mongs.wear.domain.exception.UseCaseException
import com.mongs.wear.domain.repositroy.BattleRepository
import com.mongs.wear.domain.repositroy.FeedbackRepository
import com.mongs.wear.domain.vo.MatchVo
import javax.inject.Inject

class GetMatchUseCase @Inject constructor(
    private val battleRepository: BattleRepository,
    private val feedbackRepository: FeedbackRepository,
) {
    suspend operator fun invoke(): LiveData<MatchVo> {
        try {
            return battleRepository.getMatchLive()
        } catch (e: RepositoryException) {
            feedbackRepository.addFeedbackLog(
                groupCode = FeedbackCode.BATTLE.groupCode,
                location = "GetMatchUseCase",
                message = e.stackTrace.contentDeepToString(),
            )

            throw UseCaseException(e.errorCode, e)
        }
    }
}