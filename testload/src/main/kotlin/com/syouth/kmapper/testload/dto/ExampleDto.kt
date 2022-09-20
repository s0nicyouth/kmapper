package com.syouth.kmapper.testload.dto

import com.syouth.kmapper.testload.domain.InnerDomain

internal data class ExampleDto(
    val long: Long,
    val short: Short,
    val byte: Byte,
    val float: Float,
    val double: Double,
    val list: List<Int>,
    val dataClass: InnerDto,
    val innerList: List<InnerDto>,
    val nullableInt: Int?,
    val nonNullableInt: Int,
    val innerNullableList: List<InnerDto?>,
    val nullableDataClass: InnerDto?,
    val collectionList: List<InnerDomain>,
    val intToString: Int,
    val innerMappingDomain: InnerMappingDto,
    val listNlb: List<Float?>?,
)
