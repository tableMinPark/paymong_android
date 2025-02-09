package com.mongs.wear.data.user.repository

import com.mongs.wear.core.exception.data.CreateMapCollectionsException
import com.mongs.wear.core.exception.data.GetMapCollectionsException
import com.mongs.wear.core.exception.data.GetMongCollectionsException
import com.mongs.wear.data.global.utils.HttpUtil
import com.mongs.wear.data.user.api.CollectionApi
import com.mongs.wear.data.user.dto.request.CreateCollectionMapRequestDto
import com.mongs.wear.domain.collection.model.CollectionModel
import com.mongs.wear.domain.collection.repository.CollectionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionRepositoryImpl @Inject constructor(
    private val collectionApi: CollectionApi,
): CollectionRepository {

    /**
     * 컬렉션 맵 등록
     * @throws CreateMapCollectionsException
     */
    override suspend fun createMapCollection(latitude: Double, longitude: Double) {

        val response = collectionApi.createCollectionMap(
            createCollectionMapRequestDto = CreateCollectionMapRequestDto(
                latitude = latitude,
                longitude = longitude,
            )
        )

        if (!response.isSuccessful) {
            throw CreateMapCollectionsException(result = HttpUtil.getErrorResult(response.errorBody()))
        }
    }

    /**
     * 컬렉션 맵 목록 조회
     * @throws GetMapCollectionsException
     */
    override suspend fun getMapCollections(): List<CollectionModel> {

        val response = collectionApi.getCollectionMaps()

        if (response.isSuccessful) {
            response.body()?.let { body ->
                return body.result.map {
                    CollectionModel(
                        code = it.mapTypeCode,
                        name = it.mapTypeName,
                        isIncluded = it.isIncluded,
                    )
                }
            }
        }

        throw GetMapCollectionsException(result = HttpUtil.getErrorResult(response.errorBody()))
    }

    /**
     * 컬렉션 몽 목록 조회
     * @throws GetMongCollectionsException
     */
    override suspend fun getMongCollections(): List<CollectionModel> {

        val response = collectionApi.getCollectionMongs()

        if (response.isSuccessful) {
            response.body()?.let { body ->
                return body.result.map {
                    CollectionModel(
                        code = it.mongTypeCode,
                        name = it.mongTypeName,
                        isIncluded = it.isIncluded,
                    )
                }
            }
        }

        throw GetMongCollectionsException(result = HttpUtil.getErrorResult(response.errorBody()))
    }
}