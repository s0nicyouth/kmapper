package com.syouth.kmapper.data

import com.syouth.kmapper.domain.Logger
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
internal object DependenciesContainer {

    val mockDataHolder = MockDataHolder()

    val api: MockNetworkApi = MockNetworkApiImpl(mockDataHolder)

    val mappersHolder = MappersHolder()

    val logger: Logger = Logger.PrintLogger()

    val mainRepository = MainRepositoryImpl(api, mappersHolder, logger)

}