package com.mongs.wear.domain.collection.usecase

import com.mongs.wear.core.exception.data.CreateMapCollectionsException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.CreateMapCollectionsUseCaseException
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.domain.collection.repository.CollectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateMapCollectionUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository,
) : BaseParamUseCase<CreateMapCollectionUseCase.Param, Unit>() {

    /**
     * 맵 컬렉션 등록 UseCase
     * @throws CreateMapCollectionException
     */
    override suspend fun execute(param: Param) {
        withContext(Dispatchers.IO) {
            collectionRepository.createMapCollection(
                latitude = 0.0,
                longitude = 0.0,
            )
        }
    }

    data class Param(

        val latitude: Double,

        val longitude: Double,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is CreateMapCollectionsException -> throw CreateMapCollectionsUseCaseException()

            else -> throw CreateMapCollectionsUseCaseException()
        }
    }
}