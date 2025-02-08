package com.mongs.wear.data.activity.api

import com.mongs.wear.core.dto.response.ResponseDto
import com.mongs.wear.data.activity.dto.response.GetBattleResponseDto
import com.mongs.wear.data.activity.dto.response.GetBattleRewardResponseDto
import com.mongs.wear.data.activity.dto.response.OverBattleResponseDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BattleApi {

    /**
     * 배틀 정보 조회
     */
    @GET("activity/battle/match/{roomId}")
    suspend fun getBattle(@Path("roomId") roomId: Long) : Response<ResponseDto<GetBattleResponseDto>>

    /**
     * 종료 배틀 정보 조회
     */
    @GET("activity/battle/match/over/{roomId}")
    suspend fun getOverBattle(@Path("roomId") roomId: Long) : Response<ResponseDto<OverBattleResponseDto>>

    /**
     * 배틀 보상 정보 조회
     */
    @GET("activity/battle/match")
    suspend fun getBattleReward() : Response<ResponseDto<GetBattleRewardResponseDto>>

    /**
     * 배틀 매칭 대기열 등록
     */
    @POST("activity/battle/match/wait/{mongId}")
    suspend fun createWaitMatching(@Path("mongId") mongId: Long) : Response<ResponseDto<Void>>

    /**
     * 배틀 매칭 대기열 삭제
     */
    @DELETE("activity/battle/match/wait/{mongId}")
    suspend fun deleteWaitMatching(@Path("mongId") mongId: Long) : Response<ResponseDto<Void>>
}