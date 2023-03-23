package com.syouth.kmapper.testload.domain.interfaceConvertersTest

import java.math.BigDecimal

data class InterfaceTestDomain(
    val floatToInt: Int,
    val doubleToFloat: Float?,
    val strToBigDecimal: BigDecimal,
    val bigDecimalToStr: String?
)