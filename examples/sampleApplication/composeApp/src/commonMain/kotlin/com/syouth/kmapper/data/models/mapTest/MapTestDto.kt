package com.syouth.kmapper.data.models.mapTest

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.syouth.kmapper.data.models.Creator

internal data class MapTestDto(
    val mapOne: Map<String, Int>,
    val mapTwo: Map<Long, BigDecimal>,
    val mapThree: HashMap<Int, String>
) {
    companion object : Creator<MapTestDto> {
        override fun create() = MapTestDto(
            mapOne = mapOf("one" to 1, "two" to 2),
            mapTwo = mapOf(1L to BigDecimal.ONE, 2L to BigDecimal.TWO),
            mapThree = HashMap()
        )
    }
}