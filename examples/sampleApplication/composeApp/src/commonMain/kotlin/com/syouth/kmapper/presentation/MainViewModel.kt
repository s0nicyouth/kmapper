package com.syouth.kmapper.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.syouth.kmapper.data.MockDataHolder
import com.syouth.kmapper.domain.Logger
import com.syouth.kmapper.domain.MainRepository
import com.syouth.kmapper.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.uuid.ExperimentalUuidApi

internal data class MainViewState(
    val result: Result<Any> = Result.Loading
)

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
internal class MainViewModel private constructor(
    private val mainRepository: MainRepository,
    private val mockDataHolder: MockDataHolder,
    private val logger: Logger
) : ViewModel() {

    private val current = MutableStateFlow(mockDataHolder.randomDtoKClass)

    val viewState = current
        .flatMapLatest { dataType ->
            mainRepository.observeData(dataType)
        }
        .map { result ->
            val viewState = MainViewState(result)
            logger.log(message = "view state: $viewState")
            viewState
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = MainViewState()
        )


    fun onRefresh() {
        current.value = mockDataHolder.randomDtoKClass
    }

    companion object {
        fun create(
            mainRepository: MainRepository,
            mockDataHolder: MockDataHolder,
            logger: Logger
        ) = viewModelFactory {
            initializer {
                MainViewModel(mainRepository, mockDataHolder, logger)
            }
        }
    }
}