package com.syouth.kmapper.testload.domain

internal data class ExampleDomain(
    val integer: Int,
    val long: Long,
    val short: Short,
    val byte: Byte,
    val float: Float,
    val double: Double?,
    val list: List<Int>,
    val dataClass: InnerDomain,
    val innerList: List<InnerDomain>,
    val defaultParameter: Int = 10,
    val nullableInt: Int?,
    val nonNullableInt: Int?,
    val innerNullableList: List<InnerDomain?>,
    val nullableDataClass: InnerDomain?,
    val collectionList: Collection<InnerDomain>,
    val intToString: String,
    val innerMappingDomain: InnerMappingDomain
)
