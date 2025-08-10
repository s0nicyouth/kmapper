@file:OptIn(ExperimentalUuidApi::class)

package com.syouth.kmapper.domain.models.pojoClassTest

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class DomainUser(
    val id: Uuid,
    val login: String,
    val firstname: String,
    val lastname: String,
    val addresses: List<DomainAddress>
)

data class DomainAddress(
    val id: Uuid,
    val street: String,
    val town: String
)
