package com.mongs.wear.domain.feedback.repository

interface FeedbackRepository {

    /**
     * 오류 신고 등록
     * @throws CreateFeedbackException
     */
    suspend fun createFeedback(title: String, content: String)
}