package com.mongs.wear.domain.management.repository

import androidx.lifecycle.LiveData
import com.mongs.wear.domain.management.model.MongModel

interface SlotRepository {

    /**
     * 현재 몽 설정 (슬롯 설정)
     */
    suspend fun setCurrentSlot(mongId: Long)

    /**
     * 현재 몽 조회 (슬롯 조회)
     */
    suspend fun getCurrentSlot(): MongModel?

    /**
     * 현재 몽 라이브 객체 조회 (실시간 변경 슬롯 조회)
     */
    suspend fun getCurrentSlotLive(): LiveData<MongModel?>

    /**
     * 현재 몽 정보 갱신
     */
    suspend fun updateCurrentSlot()
}