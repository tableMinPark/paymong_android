package com.mongs.wear.presentation.global.manager

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject


class LocationSensorManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        private const val TAG = "LocationSensorManager"
    }

    /**
     * 일발성 조회
     */
    private val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getLocation(): LocationVo? {
        // 이전 위치 정보 삭제
        mFusedLocationClient.flushLocations()

        val locationResult = mFusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).await()

        return locationResult?.let { location ->
            Log.i(TAG, "[Manager] GPS GEO : ${location.latitude}, ${location.longitude}, ${ LocalDateTime.ofInstant(Instant.ofEpochMilli(location.time), ZoneId.systemDefault())}")

            LocationVo(
                latitude = location.latitude,
                longitude = location.longitude,
            )
        }
    }

    /**
     * 위치 정보 Vo
     */
    data class LocationVo(

        val latitude: Double,

        val longitude: Double,
    )
}