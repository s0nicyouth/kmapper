package com.syouth.kmapper.testload.domain.recursiveDataClassTest

import java.math.BigDecimal

internal data class RecursiveDataClassDomain(
    val second: RecursiveDataClassDomainSecond
)

internal data class RecursiveDataClassDomainSecond(
    val third: RecursiveDataClassDomainThird,
    val bigDecimalToInt: BigDecimal
)

internal data class RecursiveDataClassDomainThird(
    val bigDecimalToFloat: Float,
    val stringToString: String?,
    val bigDecimalToBigDecimal: BigDecimal
)