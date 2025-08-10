package com.syouth.kmapper.domain.models.moneyMapperTest

data class Currency(
    val code: String,
    private val fractionalDigits: Int? = null
)
