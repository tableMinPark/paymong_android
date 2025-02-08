package com.mongs.wear.data.activity.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mongs.wear.data.activity.entity.MatchRoomEntity

@Dao
interface MatchRoomDao {

    /**
     * SELECT
     */
    @Query("SELECT * FROM mongs_match_room WHERE deviceId = :deviceId")
    fun findByDeviceId(deviceId: String) : MatchRoomEntity?

    @Query("SELECT * FROM mongs_match_room WHERE deviceId = :deviceId")
    fun findLiveByDeviceId(deviceId: String) : LiveData<MatchRoomEntity?>

    @Query("SELECT * FROM mongs_match_room WHERE roomId = :roomId")
    fun findByRoomId(roomId: Long) : MatchRoomEntity?

    /**
     * DELETE
     */
    @Query("DELETE FROM mongs_match_room")
    fun deleteAll()

    /**
     * INSERT
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(matchRoomEntity: MatchRoomEntity) : Long

    /**
     * UPDATE
     */
    @Update
    fun update(matchRoomEntity: MatchRoomEntity) : Int

    /**
     * INSERT & UPDATE
     */
    @Transaction
    fun save(matchRoomEntity: MatchRoomEntity) : MatchRoomEntity {

        if (this.insert(matchRoomEntity = matchRoomEntity) == -1L) {
            this.update(matchRoomEntity = matchRoomEntity)
        }

        return matchRoomEntity
    }
}