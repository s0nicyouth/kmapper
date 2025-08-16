package com.syouth.kmapper.data.models.interfaceConvertersTest

import com.syouth.kmapper.data.models.Creator

internal data class InterfaceTestDto(
    val floatToInt: Float,
    val doubleToFloat: Double,
    val strToBigDecimal: String,
    val bigDecimalToString: String?
) {
    companion object : Creator<InterfaceTestDto> {
        override fun create() = InterfaceTestDto(
            floatToInt = 4.5f,
            doubleToFloat = 6.7,
            strToBigDecimal = "constituam",
            bigDecimalToString = "eripuit"
        )
    }
}