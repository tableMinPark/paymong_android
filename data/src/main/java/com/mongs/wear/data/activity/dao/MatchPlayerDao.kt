package com.mongs.wear.data.activity.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mongs.wear.data.activity.entity.MatchPlayerEntity

@Dao
interface MatchPlayerDao {

    /**
     * SELECT
     */
    @Query("SELECT * FROM mongs_match_player WHERE playerId = :playerId")
    fun findByPlayerId(playerId: String) : MatchPlayerEntity?

    @Query("SELECT playerId FROM mongs_match_player WHERE isMe = true")
    fun findPlayerIdByIsMeTrue() : String?

    @Query("SELECT * FROM mongs_match_player WHERE isMe = true")
    fun findLiveByPlayerIdIsMeTrue() : LiveData<MatchPlayerEntity?>

    @Query("SELECT * FROM mongs_match_player WHERE isMe = false")
    fun findLiveByPlayerIdIsMeFalse() : LiveData<MatchPlayerEntity?>

    /**
     * DELETE
     */
    @Query("DELETE FROM mongs_match_player")
    fun deleteAll()

    /**
     * INSERT
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(matchPlayerEntity: MatchPlayerEntity) : Long

    /**
     * UPDATE
     */
    @Update
    fun update(matchPlayerEntity: MatchPlayerEntity) : Int

    /**
     * INSERT & UPDATE
     */
    @Transaction
    fun save(matchPlayerEntity: MatchPlayerEntity) : MatchPlayerEntity {

        if (this.insert(matchPlayerEntity = matchPlayerEntity) == -1L) {
            this.update(matchPlayerEntity = matchPlayerEntity)
        }

        return matchPlayerEntity
    }
}