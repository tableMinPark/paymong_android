package com.mongs.wear.domain.collection.usecase

import com.mongs.wear.core.exception.data.GetMongCollectionsException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.GetMongCollectionUseCaseException
import com.mongs.wear.domain.collection.repository.CollectionRepository
import com.mongs.wear.domain.collection.vo.MongCollectionVo
import com.mongs.wear.core.usecase.BaseNoParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMongCollectionsUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository,
) : BaseNoParamUseCase<List<MongCollectionVo>>() {

    /**
     * 몽 컬렉션 목록 조회 UseCase
     * @throws GetMongCollectionsException
     */
    override suspend fun execute(): List<MongCollectionVo> {
        return withContext(Dispatchers.IO) {
            collectionRepository.getMongCollections().map {
                MongCollectionVo(
                    code = it.code,
                    name = it.name,
                    isIncluded = it.isIncluded
                )
            }
        }
    }

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when(exception) {
            is GetMongCollectionsException -> throw GetMongCollectionUseCaseException()

            else -> throw GetMongCollectionUseCaseException()
        }
    }
}