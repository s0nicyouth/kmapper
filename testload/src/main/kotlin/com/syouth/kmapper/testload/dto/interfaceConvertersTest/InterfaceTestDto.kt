package com.syouth.kmapper.testload.dto.interfaceConvertersTest

import java.math.BigDecimal

data class InterfaceTestDto(
    val floatToInt: Float,
    val doubleToFloat: Double,
    val strToBigDecimal: String,
    val bigDecimalToString: BigDecimal?
)