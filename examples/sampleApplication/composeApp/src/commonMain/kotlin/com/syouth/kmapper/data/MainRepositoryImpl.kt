package com.syouth.kmapper.data

import com.syouth.kmapper.domain.Logger
import com.syouth.kmapper.domain.MainRepository
import com.syouth.kmapper.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.reflect.KClass


internal class MainRepositoryImpl(
    private val networkApi: MockNetworkApi,
    private val mappersHolder: MappersHolder,
    private val logger: Logger,
) : MainRepository {

    override fun <T : Any> observeData(type: KClass<T>): Flow<Result<Any>> =
        networkApi.getData(type)
            .map { dto: T ->
                logger.log(message = "Received DTO ${type.simpleName}")
                val domainModel = mappersHolder
                    .getMapperByDtoType<T, Any>(type)
                    .map(dto)
                logger.log(
                    message = "Received domain model ${domainModel::class.simpleName}"
                )
                Result.Success(domainModel) as Result<*>
            }
            .onStart { emit(Result.Loading) }
            .catch { error -> emit(Result.Error(error)) }

}