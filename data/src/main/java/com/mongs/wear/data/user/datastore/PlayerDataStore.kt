package com.mongs.wear.data.user.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val MEMBER_DATA_STORE_NAME = "MEMBER"

        private val STAR_POINT = intPreferencesKey("STAR_POINT")
    }

    private val Context.store by preferencesDataStore(name = MEMBER_DATA_STORE_NAME)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            context.store.edit { preferences ->

                preferences[STAR_POINT] = 0
            }
        }
    }

    suspend fun setStarPoint(starPoint: Int) {
        context.store.edit { preferences ->
            preferences[STAR_POINT] = starPoint
        }
    }

    fun getStarPointLive(): LiveData<Int> {
        return context.store.data.map { preferences ->
            preferences[STAR_POINT]!!
        }.asLiveData()
    }
}