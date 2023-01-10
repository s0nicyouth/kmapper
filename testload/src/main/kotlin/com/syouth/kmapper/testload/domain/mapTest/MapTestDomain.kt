package com.syouth.kmapper.testload.domain.mapTest

import java.math.BigDecimal

internal data class MapTestDomain(
    val mapOne: Map<String, Int>,
    val mapTwo: Map<Int, Long>,
    val mapThree: HashMap<Long, BigDecimal>
)