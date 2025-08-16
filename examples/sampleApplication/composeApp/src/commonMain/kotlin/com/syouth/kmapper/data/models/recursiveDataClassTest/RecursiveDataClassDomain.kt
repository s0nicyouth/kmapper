package com.syouth.kmapper.data.models.recursiveDataClassTest

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class RecursiveDataClassDto(
    val second: RecursiveDataClassDtoSecond
)

data class RecursiveDataClassDtoSecond(
    val third: RecursiveDataClassDtoThird,
    val bigDecimalToInt: BigDecimal
)

data class RecursiveDataClassDtoThird(
    val bigDecimalToFloatRenamed: BigDecimal,
    val stringToStringRenamed: String?,
    val bigDecimalToBigDecimal: BigDecimal
)