package com.mongs.wear.data.manager.resolver

import androidx.room.Transaction
import com.mongs.wear.data.global.room.RoomDB
import com.mongs.wear.data.manager.dto.response.MongBasicDto
import com.mongs.wear.data.manager.dto.response.MongStateDto
import com.mongs.wear.data.manager.dto.response.MongStatusDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManagementObserveResolver @Inject constructor(
    private val roomDB: RoomDB,
) {

    /**
     * 몽 정보 변경 옵저빙 함수
     */
    @Transaction
    fun updateMongBasic(mongBasicDto: MongBasicDto) {

        roomDB.mongDao().findByMongId(mongId = mongBasicDto.mongId)?.let { mongEntity ->
            roomDB.mongDao().save(
                mongEntity.updateBasic(
                    mongBasicDto = mongBasicDto,
                )
            )
        }
    }

    /**
     * 몽 상태 변경 옵저빙 함수
     */
    @Transaction
    fun updateMongState(mongStateDto: MongStateDto) {

        roomDB.mongDao().findByMongId(mongId = mongStateDto.mongId)?.let { mongEntity ->
            roomDB.mongDao().save(
                mongEntity.updateState(
                    mongStateDto = mongStateDto,
                )
            )
        }
    }

    /**
     * 몽 지수 변경 옵저빙 함수
     */
    @Transaction
    fun updateMongStatus(mongStatusDto: MongStatusDto) {

        roomDB.mongDao().findByMongId(mongId = mongStatusDto.mongId)?.let { mongEntity ->
            roomDB.mongDao().save(
                mongEntity.updateStatus(
                    mongStatusDto = mongStatusDto,
                )
            )
        }
    }
}