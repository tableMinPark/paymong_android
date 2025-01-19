package com.mongs.wear.data.manager.repository

import com.mongs.wear.data.global.room.RoomDB
import com.mongs.wear.data.global.utils.HttpUtil
import com.mongs.wear.data.manager.api.ManagementApi
import com.mongs.wear.data.manager.dto.request.CreateMongRequestDto
import com.mongs.wear.data.manager.dto.request.FeedMongRequestDto
import com.mongs.wear.data.manager.entity.MongEntity
import com.mongs.wear.data.manager.exception.CreateMongException
import com.mongs.wear.data.manager.exception.DeleteMongException
import com.mongs.wear.data.manager.exception.EvolutionMongException
import com.mongs.wear.data.manager.exception.FeedMongException
import com.mongs.wear.data.manager.exception.GetFeedItemsException
import com.mongs.wear.data.manager.exception.GraduateMongException
import com.mongs.wear.data.manager.exception.PoopCleanMongException
import com.mongs.wear.data.manager.exception.SleepMongException
import com.mongs.wear.data.manager.exception.StrokeMongException
import com.mongs.wear.domain.management.model.FeedItemModel
import com.mongs.wear.domain.management.model.MongModel
import com.mongs.wear.domain.management.repository.ManagementRepository
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManagementRepositoryImpl @Inject constructor(
    private val httpUtil: HttpUtil,
    private val roomDB: RoomDB,
    private val managementApi: ManagementApi,
): ManagementRepository {

    /**
     * 몽 정보 동기화
     */
    override suspend fun getMongs(): List<MongModel> {

        val response = managementApi.getMongs()

        if (response.isSuccessful) {
            response.body()?.let { body ->
                roomDB.mongDao().let { dao ->
                    // 없는 Mong 삭제
                    body.result.let { getMongResponseDtos ->
                        dao.deleteByMongIdNotIn(getMongResponseDtos.map { getMongResponseDto -> getMongResponseDto.basic.mongId })
                    }

                    // 현재 몽 목록 동기화
                    body.result.forEach({ getMongResponseDto ->
                        // 수정
                        dao.findByMongId(getMongResponseDto.basic.mongId)?.let { mongEntity ->
                            dao.save(
                                mongEntity.update(
                                    mongBasicDto = getMongResponseDto.basic,
                                    mongStateDto = getMongResponseDto.state,
                                    mongStatusDto = getMongResponseDto.status,
                                )
                            )
                        } ?: run {
                            // 등록
                            dao.save(
                                MongEntity.of(
                                    mongBasicDto = getMongResponseDto.basic,
                                    mongStateDto = getMongResponseDto.state,
                                    mongStatusDto = getMongResponseDto.status,
                                )
                            )
                        }
                    })
                }
            }
        }

        return roomDB.mongDao().findAll().map { mongEntity -> mongEntity.toMongModel() }
    }

    /**
     * 몽 생성
     */
    override suspend fun createMong(name: String, sleepStart: String, sleepEnd: String) {

        val response = managementApi.createMong(createMongRequestDto = CreateMongRequestDto(
            name = name,
            sleepAt = LocalTime.parse(sleepStart),
            wakeupAt = LocalTime.parse(sleepEnd),
        ));

        if (!response.isSuccessful) {
            throw CreateMongException(result = httpUtil.getErrorResult(errorBody = response.errorBody()))
        }
    }

    /**
     * 몽 삭제
     */
    override suspend fun deleteMong(mongId: Long) {

        val response = managementApi.deleteMong(mongId = mongId)

        if (!response.isSuccessful) {
            throw DeleteMongException(result = httpUtil.getErrorResult(errorBody = response.errorBody()))
        } else {
            roomDB.mongDao().deleteByMongId(mongId = mongId)
        }
    }

    /**
     * 먹이 목록 조회
     */
    override suspend fun getFeedItems(mongId: Long, foodTypeGroupCode: String): List<FeedItemModel> {

        val response = managementApi.getFeedItems(mongId = mongId, foodTypeGroupCode = foodTypeGroupCode)

        if (response.isSuccessful) {
            response.body()?.let { body ->

                return body.result.feedItems.map {
                    FeedItemModel(
                        foodTypeCode = it.foodTypeCode,
                        foodTypeGroupCode = it.foodTypeGroupCode,
                        foodTypeName = it.foodTypeName,
                        price = it.price,
                        isCanBuy = it.isCanBuy,
                        addWeightValue = it.addWeightValue,
                        addStrengthValue = it.addStrengthValue,
                        addSatietyValue = it.addSatietyValue,
                        addHealthyValue = it.addHealthyValue,
                        addFatigueValue = it.addFatigueValue,
                    )
                }
            }
        }

        throw GetFeedItemsException(result = httpUtil.getErrorResult(errorBody = response.errorBody()))
    }

    /**
     * 몽 먹이 주기
     */
    override suspend fun feedMong(mongId: Long, foodTypeCode: String) {

        val response = managementApi.feedMong(mongId = mongId, feedMongRequestDto = FeedMongRequestDto(foodTypeCode = foodTypeCode))

        if (!response.isSuccessful) {
            throw FeedMongException(result = httpUtil.getErrorResult(errorBody = response.errorBody()))
        }
    }

    /**
     * 몽 졸업
     */
    override suspend fun graduateMong(mongId: Long) {

        val response = managementApi.graduateMong(mongId = mongId)

        if (!response.isSuccessful) {
            throw GraduateMongException(result = httpUtil.getErrorResult(errorBody = response.errorBody()))
        }
    }

    /**
     * 몽 졸업 체크
     */
    override suspend fun graduateCheckMong(mongId: Long) {

        roomDB.mongDao().findByMongId(mongId = mongId)?.let { mongEntity ->

            mongEntity.graduateCheck()

            roomDB.mongDao().save(mongEntity)
        }
    }

    /**
     * 몽 진화
     */
    override suspend fun evolutionMong(mongId: Long) {

        val response = managementApi.evolutionMong(mongId = mongId)

        if (!response.isSuccessful) {
            throw EvolutionMongException(result = httpUtil.getErrorResult(errorBody = response.errorBody()))
        }
    }

    /**
     * 몽 수면/기상
     */
    override suspend fun sleepingMong(mongId: Long) {

        val response = managementApi.sleepMong(mongId = mongId)

        if (!response.isSuccessful) {
            throw SleepMongException(result = httpUtil.getErrorResult(errorBody = response.errorBody()))
        }
    }

    /**
     * 몽 쓰다 듬기
     */
    override suspend fun strokeMong(mongId: Long) {

        val response = managementApi.strokeMong(mongId = mongId)

        if (!response.isSuccessful) {
            throw StrokeMongException(result = httpUtil.getErrorResult(errorBody = response.errorBody()))
        }
    }

    /**
     * 몽 배변 처리
     */
    override suspend fun poopCleanMong(mongId: Long) {

        val response = managementApi.poopCleanMong(mongId = mongId)

        if (!response.isSuccessful) {
            throw PoopCleanMongException(result = httpUtil.getErrorResult(errorBody = response.errorBody()))
        }
    }
}