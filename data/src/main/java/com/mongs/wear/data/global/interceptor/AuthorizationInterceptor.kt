package com.mongs.wear.data.global.interceptor

import com.mongs.wear.data.auth.api.AuthApi
import com.mongs.wear.data.auth.dataStore.TokenDataStore
import com.mongs.wear.data.auth.dto.request.ReissueRequestDto
import com.mongs.wear.core.exception.data.ReissueException
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor (
    private val authApi: AuthApi,
    private val tokenDataStore: TokenDataStore,
) : Interceptor {

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"

        private const val CONNECTION_FORBIDDEN_CODE = 401
    }

    /**
     * 엑세스 토큰 인터 셉터
     */
    override fun intercept(chain: Chain): Response {

        val accessToken = runBlocking { tokenDataStore.getAccessToken() }

        val response = chain.proceed(this.generateRequest(chain, accessToken))

        if (response.code() == CONNECTION_FORBIDDEN_CODE) {

            val newAccessToken = runBlocking { reissue() }

            return chain.proceed(this.generateRequest(chain, newAccessToken))

        } else {
            return response
        }
    }

    /**
     * Http 요청 생성
     */
    private fun generateRequest(chain: Chain, accessToken: String) : Request = chain.request().newBuilder()
        .addHeader(AUTHORIZATION_HEADER, "Bearer $accessToken")
        .build()

    /**
     * 토큰 재발행
     */
    private suspend fun reissue() : String {

        val refreshToken = tokenDataStore.getRefreshToken()

        val response = authApi.reissue(ReissueRequestDto(refreshToken = refreshToken))

        if (response.isSuccessful) {
            response.body()?.let { body ->

                tokenDataStore.setAccessToken(accessToken = body.result.accessToken)
                tokenDataStore.setRefreshToken(refreshToken = body.result.refreshToken)

                return body.result.accessToken
            }
        }

        throw ReissueException()
    }
}