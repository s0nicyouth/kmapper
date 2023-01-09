package com.syouth.kmapper.testload.dto.recursiveDataClassTest

import java.math.BigDecimal

internal data class RecursiveDataClassDto(
    val second: RecursiveDataClassDtoSecond
)

internal data class RecursiveDataClassDtoSecond(
    val third: RecursiveDataClassDtoThird,
    val bigDecimalToInt: BigDecimal
)

internal data class RecursiveDataClassDtoThird(
    val bigDecimalToFloatRenamed: BigDecimal,
    val stringToStringRenamed: String?,
    val bigDecimalToBigDecimal: BigDecimal
)