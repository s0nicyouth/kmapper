package com.syouth.kmapper.testload.domain.pojoClassTest

import java.util.UUID

data class DomainUser(
    val id: UUID,
    val login: kotlin.String,
    val firstname: String,
    val lastname: String,
    val addresses: List<DomainAddress>)

data class DomainAddress(
    val id: UUID,
    val street: String,
    val town: String
)
