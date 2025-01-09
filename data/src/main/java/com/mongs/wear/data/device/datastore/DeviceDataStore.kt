package com.mongs.wear.data.device.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val DEVICE_DATA_STORE_NAME = "DEVICE"

        private val DEVICE_ID = stringPreferencesKey("DEVICE_ID")
        private val DEVICE_BOOTED_DT = stringPreferencesKey("DEVICE_BOOTED_DT")
        private val BG_MAP_TYPE_CODE = stringPreferencesKey("BG_MAP_TYPE_CODE")
        private val NETWORK = booleanPreferencesKey("NETWORK")
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    }

    private val Context.store by preferencesDataStore(name = DEVICE_DATA_STORE_NAME)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            context.store.edit { preferences ->

                if (!preferences.contains(DEVICE_ID)) {
                    preferences[DEVICE_ID] = ""
                }

                if (!preferences.contains(DEVICE_BOOTED_DT)) {
                    preferences[DEVICE_BOOTED_DT] = LocalDateTime.of(1970, 1, 1, 0, 0, 0).format(
                        dateFormatter
                    )
                }

                if (!preferences.contains(BG_MAP_TYPE_CODE)) {
                    preferences[BG_MAP_TYPE_CODE] = "MP000"
                }

                preferences[NETWORK] = true
            }
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

    suspend fun setDeviceBootedDt(deviceBootedDt: LocalDateTime) {
        context.store.edit { preferences ->
            preferences[DEVICE_BOOTED_DT] = deviceBootedDt.format(dateFormatter)
        }
    }
    fun getDeviceBootedDt(): LocalDateTime {
        return runBlocking {
            context.store.data.map { preferences ->
                LocalDateTime.parse(preferences[DEVICE_BOOTED_DT]!!, dateFormatter)
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