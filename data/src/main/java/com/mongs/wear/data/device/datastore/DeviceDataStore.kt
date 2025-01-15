package com.mongs.wear.data.device.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val DEVICE_DATA_STORE_NAME = "DEVICE"

        private val STEPS = intPreferencesKey("STEPS")
        private val WALKING_COUNT = intPreferencesKey("WALKING_COUNT")
        private val CONSUME_WALKING_COUNT = intPreferencesKey("CONSUME_WALKING_COUNT")
        private val TOTAL_WALKING_COUNT = intPreferencesKey("TOTAL_WALKING_COUNT")

        private val DEVICE_ID = stringPreferencesKey("DEVICE_ID")
        private val BG_MAP_TYPE_CODE = stringPreferencesKey("BG_MAP_TYPE_CODE")
        private val NETWORK = booleanPreferencesKey("NETWORK")
    }

    private val Context.store by preferencesDataStore(name = DEVICE_DATA_STORE_NAME)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            context.store.edit { preferences ->

                preferences[NETWORK] = true

                if (!preferences.contains(STEPS)) {
                    preferences[STEPS] = 0
                }

                if (!preferences.contains(WALKING_COUNT)) {
                    preferences[WALKING_COUNT] = 0
                }

                if (!preferences.contains(CONSUME_WALKING_COUNT)) {
                    preferences[CONSUME_WALKING_COUNT] = 0
                }

                if (!preferences.contains(TOTAL_WALKING_COUNT)) {
                    preferences[TOTAL_WALKING_COUNT] = 0
                }

                if (!preferences.contains(DEVICE_ID)) {
                    preferences[DEVICE_ID] = ""
                }

                if (!preferences.contains(BG_MAP_TYPE_CODE)) {
                    preferences[BG_MAP_TYPE_CODE] = "MP000"
                }
            }
        }
    }

    fun getSteps(): Int {
        return runBlocking {
            context.store.data.map { preferences ->
                preferences[STEPS]!!
            }.first()
        }
    }

    fun getStepsLive() : LiveData<Int> {
        return context.store.data.map { preferences ->
            preferences[STEPS]!!
        }.asLiveData()
    }

    suspend fun setWalkingCount(walkingCount: Int) {
        context.store.edit { preferences ->
            preferences[WALKING_COUNT] = walkingCount

            val totalWalkingCount = preferences[TOTAL_WALKING_COUNT]!!
            val consumeWalkingCount = preferences[CONSUME_WALKING_COUNT]!!

            preferences[STEPS] = walkingCount + (totalWalkingCount - consumeWalkingCount)
        }
    }

    suspend fun setConsumeWalkingCount(consumeWalkingCount: Int) {
        context.store.edit { preferences ->
            preferences[CONSUME_WALKING_COUNT] = consumeWalkingCount

            val totalWalkingCount = preferences[TOTAL_WALKING_COUNT]!!
            val walkingCount = preferences[WALKING_COUNT]!!

            preferences[STEPS] = walkingCount + (totalWalkingCount - consumeWalkingCount)
        }
    }

    fun getConsumeWalkingCount(): Int {
        return runBlocking {
            context.store.data.map { preferences ->
                preferences[CONSUME_WALKING_COUNT]!!
            }.first()
        }
    }

    suspend fun setTotalWalkingCount(totalWalkingCount: Int) {
        context.store.edit { preferences ->
            preferences[TOTAL_WALKING_COUNT] = totalWalkingCount

            val consumeWalkingCount = preferences[CONSUME_WALKING_COUNT]!!
            val walkingCount = preferences[WALKING_COUNT]!!

            preferences[STEPS] = walkingCount + (totalWalkingCount - consumeWalkingCount)
        }
    }

    suspend fun getTotalWalkingCount() : Int {
        return runBlocking {
            context.store.data.map { preferences ->
                preferences[TOTAL_WALKING_COUNT]!!
            }.first()
        }
    }

    suspend fun setDeviceId(deviceId: String) {
        context.store.edit { preferences ->
            preferences[DEVICE_ID] = deviceId
        }
    }

    fun getDeviceId() : String {
        return runBlocking {
            context.store.data.map { preferences ->
                preferences[DEVICE_ID]!!
            }.first()
        }
    }

    suspend fun setBgMapTypeCode(mapTypeCode: String) {
        context.store.edit { preferences ->
            preferences[BG_MAP_TYPE_CODE] = mapTypeCode
        }
    }

    fun getBgMapTypeCodeLive() : LiveData<String> {
        return context.store.data.map { preferences ->
            preferences[BG_MAP_TYPE_CODE]!!
        }.asLiveData()
    }

    suspend fun setNetwork(network: Boolean) {
        context.store.edit { preferences ->
            preferences[NETWORK] = network
        }
    }

    fun getNetworkLive() : LiveData<Boolean> {
        return context.store.data.map { preferences ->
            preferences[NETWORK]!!
        }.asLiveData()
    }
}