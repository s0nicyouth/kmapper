@file:OptIn(ExperimentalUuidApi::class)

package com.syouth.kmapper.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.uuid.ExperimentalUuidApi

internal interface MockNetworkApi {
    fun <T : Any> getData(dataType: KClass<T>): Flow<T>
}

internal class MockNetworkApiImpl(
    private val mockDataHolder: MockDataHolder
) : MockNetworkApi {

    override fun <T : Any> getData(dataType: KClass<T>): Flow<T> {

        return flow {
            delay(Random.nextLong(1000, 10000))

            if (Random.nextInt(0, 100) < 30) {
                throw IllegalStateException("Something went wrong")
            }
            emit(mockDataHolder.getDtoCreatorByType(dataType))
        }
    }

}