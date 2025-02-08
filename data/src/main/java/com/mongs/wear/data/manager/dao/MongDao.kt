package com.mongs.wear.data.manager.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mongs.wear.core.enums.MongStateCode
import com.mongs.wear.core.enums.MongStatusCode
import com.mongs.wear.data.manager.entity.MongEntity
import java.time.LocalDateTime

@Dao
interface MongDao {

    /**
     * SELECT
     */
    @Query("SElECT * FROM mongs_mong WHERE mongId = :mongId")
    fun findByMongId(mongId: Long) : MongEntity?

    @Query("SElECT * FROM mongs_mong WHERE mongId = :mongId")
    fun findLiveByMongId(mongId: Long) : LiveData<MongEntity?>

    @Query("SELECT * FROM mongs_mong WHERE isCurrent = true")
    fun findByIsCurrentTrue() : MongEntity?

    @Query("SELECT * FROM mongs_mong WHERE isCurrent = true")
    fun findAllByIsCurrentTrue() : List<MongEntity>

    @Query("SELECT * FROM mongs_mong")
    fun findAll() : List<MongEntity>

    /**
     * DELETE
     */
    @Query("DELETE FROM mongs_mong WHERE mongId = :mongId")
    fun deleteByMongId(mongId: Long)

    @Query("DELETE FROM mongs_mong WHERE mongId NOT IN (:mongIds)")
    fun deleteByMongIdNotIn(mongIds: List<Long>)

    /**
     * UPDATE
     */
    @Query("""
        UPDATE mongs_mong SET mongName = :mongName
            , mongTypeCode = :mongTypeCode
            , payPoint = :payPoint
            , basicUpdatedAt = :updatedAt
         WHERE mongId = :mongId
    """)
    fun updateMongBasic(mongId: Long, mongName: String, mongTypeCode: String, payPoint: Int, updatedAt: LocalDateTime)

    @Query("""
        UPDATE mongs_mong SET stateCode = :stateCode
            , isSleeping = :isSleeping
            , stateUpdatedAt = :updatedAt
         WHERE mongId = :mongId
    """)
    fun updateMongState(mongId: Long, stateCode: MongStateCode, isSleeping: Boolean, updatedAt: LocalDateTime)

    @Query("""
        UPDATE mongs_mong SET statusCode = :statusCode
            , weight = :weight
            , expRatio = :expRatio
            , healthyRatio = :healthyRatio
            , satietyRatio = :satietyRatio
            , strengthRatio = :strengthRatio
            , fatigueRatio = :fatigueRatio
            , poopCount = :poopCount
            , statusUpdatedAt = :updatedAt
         WHERE mongId = :mongId
    """)
    fun updateMongStatus(mongId: Long, statusCode: MongStatusCode, weight: Double, expRatio: Double, healthyRatio: Double, satietyRatio: Double, strengthRatio: Double, fatigueRatio: Double, poopCount: Int, updatedAt: LocalDateTime)

    @Query("""
        UPDATE mongs_mong SET isCurrent = :isCurrent
            , graduateCheck = :graduateCheck
         WHERE mongId = :mongId
    """)
    fun updateMongLocal(mongId: Long, isCurrent: Boolean, graduateCheck: Boolean)

    /**
     * INSERT
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(mongEntity: MongEntity) : Long

    /**
     * Entity 갱신
     */
    @Transaction
    fun save(mongEntity: MongEntity) : MongEntity {
        // 기존에 있는 데이터의 경우
        if (this.insert(mongEntity = mongEntity) == -1L) {
            this.findByMongId(mongId = mongEntity.mongId)?.let { nowMongEntity ->
                // 몽 기본 정보 업데이트
                if (mongEntity.basicUpdatedAt.isAfter(nowMongEntity.basicUpdatedAt) || mongEntity.basicUpdatedAt.isEqual(nowMongEntity.basicUpdatedAt)) {
                    this.updateMongBasic(
                        mongId = mongEntity.mongId,
                        mongName = mongEntity.mongName,
                        mongTypeCode = mongEntity.mongTypeCode,
                        payPoint = mongEntity.payPoint,
                        updatedAt = mongEntity.basicUpdatedAt,
                    )
                }

                // 몽 상태 정보 업데이트
                if (mongEntity.stateUpdatedAt.isAfter(nowMongEntity.stateUpdatedAt) || mongEntity.stateUpdatedAt.isEqual(nowMongEntity.stateUpdatedAt)) {
                    this.updateMongState(
                        mongId = mongEntity.mongId,
                        stateCode = mongEntity.stateCode,
                        isSleeping = mongEntity.isSleeping,
                        updatedAt = mongEntity.stateUpdatedAt
                    )
                }

                // 몽 지수 정보 업데이트
                if (mongEntity.statusUpdatedAt.isAfter(nowMongEntity.statusUpdatedAt) || mongEntity.statusUpdatedAt.isEqual(nowMongEntity.statusUpdatedAt)) {
                    this.updateMongStatus(
                        mongId = mongEntity.mongId,
                        statusCode = mongEntity.statusCode,
                        weight = mongEntity.weight,
                        expRatio = mongEntity.expRatio,
                        healthyRatio = mongEntity.healthyRatio,
                        satietyRatio = mongEntity.satietyRatio,
                        strengthRatio = mongEntity.strengthRatio,
                        fatigueRatio = mongEntity.fatigueRatio,
                        poopCount = mongEntity.poopCount,
                        updatedAt = mongEntity.statusUpdatedAt
                    )
                }

                // 로컬 필드 업데이트
                this.updateMongLocal(
                    mongId = mongEntity.mongId,
                    isCurrent = mongEntity.isCurrent,
                    graduateCheck = mongEntity.graduateCheck
                )
            }
        }

        return mongEntity
    }
}