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

    @Query("SELECT * FROM mongs_match_room WHERE deviceId = :deviceId")
    fun findByDeviceId(deviceId: String) : MatchRoomEntity?

    @Query("SELECT * FROM mongs_match_room WHERE deviceId = :deviceId")
    fun findLiveByDeviceId(deviceId: String) : LiveData<MatchRoomEntity?>

    @Query("SELECT * FROM mongs_match_room WHERE roomId = :roomId")
    fun findByRoomId(roomId: Long) : MatchRoomEntity?

    @Query("DELETE FROM mongs_match_room")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(matchRoomEntity: MatchRoomEntity) : Long

    @Update
    fun update(matchRoomEntity: MatchRoomEntity) : Int

    @Transaction
    fun save(matchRoomEntity: MatchRoomEntity) : MatchRoomEntity {

        val isSuccess = this.insert(matchRoomEntity = matchRoomEntity)

        if (isSuccess == -1L) this.update(matchRoomEntity = matchRoomEntity)

        return matchRoomEntity
    }
}