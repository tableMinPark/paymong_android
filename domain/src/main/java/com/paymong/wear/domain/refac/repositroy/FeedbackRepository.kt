package com.paymong.wear.domain.refac.repositroy

interface FeedbackRepository {
    suspend fun addFeedbackLog(groupCode: String, location: String, message: String)
    suspend fun feedback(code: String, groupCode: String, message: String)
}