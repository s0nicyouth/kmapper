package com.syouth.kmapper.domain

import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

internal sealed interface Result<out T : Any> {

    data object Loading : Result<Nothing>

    data class Success<T : Any>(val data: T) : Result<T>

    data class Error(val error: Throwable) : Result<Nothing>

}

internal interface MainRepository {

    fun <T : Any> observeData(type: KClass<T>): Flow<Result<Any>>

}