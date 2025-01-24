package com.mongs.wear.data.activity.api

import com.mongs.wear.core.dto.response.ResponseDto
import com.mongs.wear.data.activity.dto.request.TrainingRunnerRequestDto
import com.mongs.wear.data.activity.dto.response.GetTrainingRunnerResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TrainingApi {

    @GET("activity/training/runner")
    suspend fun getTrainingRunner() : Response<ResponseDto<GetTrainingRunnerResponseDto>>

    @POST("activity/training/runner/{mongId}")
    suspend fun trainingRunner(@Path("mongId") mongId: Long, @Body trainingRunnerRequestDto: TrainingRunnerRequestDto) : Response<ResponseDto<Void>>
}