package com.mongs.wear.data.global.interceptor

import android.util.Log
import com.google.gson.Gson
import com.mongs.wear.core.dto.response.ResponseDto
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okio.BufferedSource
import okio.buffer
import okio.source
import java.net.ConnectException

class HttpLogInterceptor(
    private val gson: Gson
) : Interceptor {

    companion object {
        private const val TAG = "HttpLogInterceptor"

        private const val CONNECTION_RESPONSE_CODE = 500
        private const val CONNECTION_RESPONSE_MESSAGE = "서버 연결 실패"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val response = try {
             chain.proceed(chain.request())
        } catch (_: ConnectException) {
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(CONNECTION_RESPONSE_CODE)
                .message(CONNECTION_RESPONSE_MESSAGE)
                .body(ResponseBody.create(MediaType.get("application/json"), ""))
                .build()
        }

        response.body()?.let { responseBody ->

            val bodyJson = responseBody.string()

            val responseDto = gson.fromJson(bodyJson, ResponseDto::class.java)

            Log.i(TAG, "[Api] ${request.method()} ${request.url()} ==> [${response.code()}] $responseDto")

            return response.newBuilder()
                .body(RealResponseBody(body = bodyJson, originalBody = responseBody))
                .build()

        } ?: run {
            return response
        }
    }

    class RealResponseBody(
        private val body: String,
        private val originalBody: ResponseBody,
    ) : ResponseBody() {
        override fun contentType(): MediaType? = originalBody.contentType()
        override fun contentLength(): Long = originalBody.contentLength()
        override fun source(): BufferedSource = body.byteInputStream().source().buffer()
    }
}