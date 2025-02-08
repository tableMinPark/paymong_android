package com.mongs.wear.data.user.api

import com.mongs.wear.core.dto.response.ResponseDto
import com.mongs.wear.data.user.dto.response.GetCollectionMapResponseDto
import com.mongs.wear.data.user.dto.response.GetCollectionMongResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface CollectionApi {

    /**
     * 맵 컬렉션 목록 조회
     */
    @GET("user/collection/map")
    suspend fun getCollectionMaps() : Response<ResponseDto<List<GetCollectionMapResponseDto>>>

    /**
     * 몽 컬렉션 목록 조회
     */
    @GET("user/collection/mong")
    suspend fun getCollectionMongs() : Response<ResponseDto<List<GetCollectionMongResponseDto>>>
}