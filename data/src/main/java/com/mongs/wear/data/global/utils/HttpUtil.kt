package com.mongs.wear.data.global.utils

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.mongs.wear.core.adapter.GsonLocalDateTimeFormatAdapter
import com.mongs.wear.core.adapter.GsonLocalTimeAdapter
import com.mongs.wear.core.dto.response.ResponseDto
import okhttp3.ResponseBody
import java.time.LocalDateTime
import java.time.LocalTime

object HttpUtil {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, GsonLocalDateTimeFormatAdapter())
        .registerTypeAdapter(LocalTime::class.java, GsonLocalTimeAdapter())
        .create()

    /**
     * ErrorResponseDto 파싱
     */
    fun getErrorResult(errorBody: ResponseBody?) : Map<String, Any> {
        return errorBody?.let { body ->
            gson.fromJson(body.string(), ResponseDto::class.java)?.let { responseDto ->
                responseDto.result?.let { result ->
                    gson.fromJson(gson.toJson(result), object : TypeToken<Map<String, Any>>() {}.type)
                } ?: run {
                    emptyMap()
                }
            } ?: run {
                emptyMap()
            }
        } ?: run {
            emptyMap()
        }
    }
}