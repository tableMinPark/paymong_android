package com.mongs.wear.data.manager.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.mongs.wear.data.global.room.RoomDB
import com.mongs.wear.domain.management.model.MongModel
import com.mongs.wear.domain.management.repository.SlotRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SlotRepositoryImpl @Inject constructor(
    private val roomDB: RoomDB,
): SlotRepository {

    /**
     * 현재 몽 선택
     */
    override suspend fun setCurrentSlot(mongId: Long) {

        roomDB.mongDao().let { dao ->
            // 전체 선택 해제 (오류 값 보정)
            dao.findAllByIsCurrentTrue().forEach { mongEntity ->
                dao.save(mongEntity.unPick())
            }

            dao.findByMongId(mongId = mongId)?.let { mongEntity ->
                dao.save(mongEntity.pick())
            }
        }
    }

    /**
     * 현재 선택된 몽 조회
     */
    override suspend fun getCurrentSlot(): MongModel? {

        return roomDB.mongDao().findByIsCurrentTrue()?.let { currentMongEntity ->
            roomDB.mongDao().findByMongId(mongId = currentMongEntity.mongId)
                ?.toMongModel()
        } ?: run {
            null
        }
    }

    /**
     * 현재 선택된 몽 Live 조회
     */
    override suspend fun getCurrentSlotLive(): LiveData<MongModel?> {

        roomDB.mongDao().let { dao ->
            return dao.findByIsCurrentTrue()?.let { currentMongEntity ->
                dao.findLiveByMongId(mongId = currentMongEntity.mongId).map { mongEntity ->
                    mongEntity?.toMongModel() ?: run { null }
                }
            } ?: MutableLiveData(null)
        }
    }
}