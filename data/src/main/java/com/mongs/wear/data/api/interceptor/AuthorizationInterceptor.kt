package com.mongs.wear.data.api.interceptor

import com.mongs.wear.data.api.client.AuthApi
import com.mongs.wear.data.dataStore.MemberDataStore
import com.mongs.wear.data.dto.auth.req.ReissueReqDto
import com.mongs.wear.domain.error.RepositoryErrorCode
import com.mongs.wear.domain.exception.RepositoryException
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import java.lang.RuntimeException

class AuthorizationInterceptor (
    private val memberDataStore: MemberDataStore,
    private val authApi: AuthApi,
) : Interceptor {
    override fun intercept(chain: Chain): Response {
        val accessToken = runBlocking { return@runBlocking memberDataStore.getAccessToken() }
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(newRequest)

        if (response.code() == 401) {
            response.close()
            return reissueAndRetry(chain)
        }

        return response
    }
    private fun reissueAndRetry(chain: Chain) : Response {
        try {
            val accessToken = runBlocking {
                val refreshToken = memberDataStore.getRefreshToken()
                val res = authApi.reissue(
                    ReissueReqDto(
                        refreshToken = refreshToken,
                    )
                )

                if (res.isSuccessful) {
                    res.body()?.let { body ->
                        val newAccessToken = body.accessToken
                        val newRefreshToken = body.refreshToken

                        memberDataStore.setAccessToken(accessToken = newAccessToken)
                        memberDataStore.setRefreshToken(refreshToken = newRefreshToken)

                        return@runBlocking newAccessToken
                    }
                }

                throw RepositoryException(RepositoryErrorCode.REISSUE_FAIL)
            }

            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            return chain.proceed(newRequest)
        } catch (_: RuntimeException) {}

        return chain.proceed(chain.request())
    }
}