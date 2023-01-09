package com.syouth.kmapper.testload.domain.bindTest

internal data class SomeInternalDomain(
    val i: Int
)

internal data class BindDomain(
    val one: String,
    val second: Int,
    val third: SomeInternalDomain,
    val fourth: Double
)
