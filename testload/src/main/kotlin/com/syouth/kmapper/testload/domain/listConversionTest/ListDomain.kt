package com.syouth.kmapper.testload.domain.listConversionTest

internal data class OtherListDomain(
    val k: String
)

internal data class ListDomain(
    val i: OtherListDomain,
    val l: List<OtherListDomain>
)
