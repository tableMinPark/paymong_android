package com.mongs.wear.domain.management.repository

import com.mongs.wear.core.exception.data.DeleteMongException
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
     * @throws CreateMongException
     */
    suspend fun createMong(name: String, sleepStart: String, sleepEnd: String)

    /**
     * 몽 삭제
     * @throws DeleteMongException
     */
    suspend fun deleteMong(mongId: Long)

    /**
     * 몽 먹이 목록 조회
     * @throws GetFeedItemsException
     */
    suspend fun getFeedItems(mongId: Long, foodTypeGroupCode: String): List<FeedItemModel>

    /**
     * 몽 먹이 주기
     * @throws FeedMongException
     */
    suspend fun feedMong(mongId: Long, foodTypeCode: String)

    /**
     * 몽 졸업
     * @throws GraduateMongException
     */
    suspend fun graduateMong(mongId: Long)

    /**
     * 몽 졸업 체크
     */
    suspend fun graduateCheckMong(mongId: Long)

    /**
     * 몽 진화
     * @throws EvolutionMongException
     */
    suspend fun evolutionMong(mongId: Long)

    /**
     * 몽 수면/기상
     * @throws SleepMongException
     */
    suspend fun sleepingMong(mongId: Long)

    /**
     * 몽 쓰다 듬기
     * @throws StrokeMongException
     */
    suspend fun strokeMong(mongId: Long)

    /**
     * 몽 배변 처리
     * @throws PoopCleanMongException
     */
    suspend fun poopCleanMong(mongId: Long)
}