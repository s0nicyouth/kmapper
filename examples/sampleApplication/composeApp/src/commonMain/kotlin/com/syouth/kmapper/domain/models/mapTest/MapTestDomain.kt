package com.syouth.kmapper.domain.models.mapTest

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class MapTestDomain(
    val mapOne: Map<String, Int>,
    val mapTwo: Map<Int, Long>,
    val mapThree: HashMap<Long, BigDecimal>
)