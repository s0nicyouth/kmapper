package com.syouth.kmapper.testload.domain.cyclicTest

internal data class FirstDataDomain(
    val secondData: SecondDataDomain
)
internal data class SecondDataDomain(
    val firstData: FirstDataDomain
)