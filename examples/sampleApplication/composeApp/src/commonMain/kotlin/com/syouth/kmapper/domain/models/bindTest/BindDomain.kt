package com.syouth.kmapper.domain.models.bindTest

data class SomeDomain(
    val i: Int
)

data class BindDomain(
    val one: String,
    val second: Int,
    val third: SomeDomain,
    val fourth: Double
)
