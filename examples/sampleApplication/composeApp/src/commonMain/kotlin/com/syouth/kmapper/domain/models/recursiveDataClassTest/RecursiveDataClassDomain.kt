package com.syouth.kmapper.domain.models.recursiveDataClassTest

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class RecursiveDataClassDomain(
    val second: RecursiveDataClassDomainSecond
)

data class RecursiveDataClassDomainSecond(
    val third: RecursiveDataClassDomainThird,
    val bigDecimalToInt: BigDecimal
)

data class RecursiveDataClassDomainThird(
    val bigDecimalToFloat: Float,
    val stringToString: String?,
    val bigDecimalToBigDecimal: BigDecimal
)