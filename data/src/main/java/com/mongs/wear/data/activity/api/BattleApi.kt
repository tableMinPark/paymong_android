package com.mongs.wear.data.activity.api

import com.mongs.wear.core.dto.response.ResponseDto
import com.mongs.wear.data.activity.dto.response.OverBattleResponseDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BattleApi {

    @POST("activity/battle/match/wait/{mongId}")
    suspend fun createWaitMatching(@Path("mongId") mongId: Long) : Response<ResponseDto<Void>>

    @DELETE("activity/battle/match/wait/{mongId}")
    suspend fun deleteWaitMatching(@Path("mongId") mongId: Long) : Response<ResponseDto<Void>>

    @GET("activity/battle/match/{roomId}")
    suspend fun overBattle(@Path("roomId") roomId: Long) : Response<ResponseDto<OverBattleResponseDto>>
}