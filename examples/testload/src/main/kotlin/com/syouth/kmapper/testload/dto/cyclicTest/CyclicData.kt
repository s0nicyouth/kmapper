package com.syouth.kmapper.testload.dto.cyclicTest

internal data class FirstData(
    val secondData: SecondData
)

internal data class SecondData(
    val firstData: FirstData
)