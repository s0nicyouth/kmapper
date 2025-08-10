package com.syouth.kmapper.domain.models.listConversionTest

data class OtherListDomain(
    val k: String
)

data class ListDomain(
    val i: OtherListDomain,
    val l: List<OtherListDomain>
)
