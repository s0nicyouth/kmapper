package com.syouth.kmapper.processor.base.data

internal val SUPPORTED_CONVERSION_INTERFACES = setOf(
    "kotlin.collections.Iterable",
    "kotlin.collections.MutableIterable",
    "kotlin.collections.Collection",
    "kotlin.collections.MutableCollection",
    "kotlin.collections.List",
    "kotlin.collections.MutableList",
    "kotlin.collections.Set",
    "kotlin.collections.MutableSet",
)

internal val SUPPORTED_MAP_CONVERSION_INTERFACES = setOf(
    "kotlin.collections.Map",
    "kotlin.collections.MutableMap",
    "kotlin.collections.HashMap",
)

internal val SUPPORTED_CONVERSION_INTERFACES_CORRESPONDING_CONCRETE_TYPES = mapOf(
    "kotlin.collections.Iterable" to "kotlin.collections.ArrayList",
    "kotlin.collections.MutableIterable" to "kotlin.collections.ArrayList",
    "kotlin.collections.Collection" to "kotlin.collections.ArrayList",
    "kotlin.collections.MutableCollection" to "kotlin.collections.ArrayList",
    "kotlin.collections.List" to "kotlin.collections.ArrayList",
    "kotlin.collections.MutableList" to "kotlin.collections.ArrayList",
    "kotlin.collections.Set" to "kotlin.collections.HashSet",
    "kotlin.collections.MutableSet" to "kotlin.collections.HashSet",
    "kotlin.collections.Map" to "kotlin.collections.HashMap",
    "kotlin.collections.MutableMap" to "kotlin.collections.HashMap",
    "kotlin.collections.HashMap" to "kotlin.collections.HashMap",
)