package com.mongs.wear.domain.management.repository

import com.mongs.wear.domain.management.model.FeedItemModel
import com.mongs.wear.domain.management.model.MongModel

interface ManagementRepository {

    /**
     * 몽 정보 전체 동기화
     */
    suspend fun updateMongs()

    /**
     * 몽 목록 조회
     */
    suspend fun getMongs(): List<MongModel>

    /**
     * 몽 정보 동기화
     */
    suspend fun updateMong(mongId: Long)

    /**
     * 몽 생성
     */
    suspend fun createMong(name: String, sleepStart: String, sleepEnd: String)

    /**
     * 몽 삭제
     */
    suspend fun deleteMong(mongId: Long)

    /**
     * 몽 먹이 목록 조회
     */
    suspend fun getFeedItems(mongId: Long, foodTypeGroupCode: String): List<FeedItemModel>

    /**
     * 몽 먹이 주기
     */
    suspend fun feedMong(mongId: Long, foodTypeCode: String)

    /**
     * 몽 졸업
     */
    suspend fun graduateMong(mongId: Long)

    /**
     * 몽 졸업 체크
     */
    suspend fun graduateCheckMong(mongId: Long)

    /**
     * 몽 진화
     */
    suspend fun evolutionMong(mongId: Long)

    /**
     * 몽 수면/기상
     */
    suspend fun sleepingMong(mongId: Long)

    /**
     * 몽 쓰다 듬기
     */
    suspend fun strokeMong(mongId: Long)

    /**
     * 몽 배변 처리
     */
    suspend fun poopCleanMong(mongId: Long)
}