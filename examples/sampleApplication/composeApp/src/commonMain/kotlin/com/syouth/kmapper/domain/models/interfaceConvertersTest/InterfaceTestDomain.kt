package com.syouth.kmapper.domain.models.interfaceConvertersTest

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class InterfaceTestDomain(
    val floatToInt: Int,
    val doubleToFloat: Float?,
    val strToBigDecimal: BigDecimal,
    val bigDecimalToStr: String?
)