package com.mongs.wear.core.utils

import android.os.SystemClock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimeUtil {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm")

    /**
     * 기기 부팅 시간 확인
     */
    fun getBootedDt() : LocalDateTime {

        val uptimeMillis = System.currentTimeMillis() - SystemClock.elapsedRealtime()

        val deviceBootedDt = Instant.ofEpochMilli(uptimeMillis).atZone(ZoneId.systemDefault()).toLocalDateTime()

        return LocalDateTime.parse(deviceBootedDt.format(dateFormatter), dateFormatter)
    }
}