package com.syouth.kmapper.data.models.exhaustiveTest

import com.syouth.kmapper.data.models.Creator
import com.syouth.kmapper.domain.models.exhaustiveTest.InnerDomain

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
) {
    companion object : Creator<ExampleDto> {
        override fun create(): ExampleDto = ExampleDto(
            long = 0,
            short = 0,
            byte = 0,
            float = 0.0f,
            double = 0.0,
            list = emptyList(),
            dataClass = InnerDto(0, 1),
            innerList = emptyList(),
            nullableInt = null,
            nonNullableInt = 0,
            innerNullableList = emptyList(),
            nullableDataClass = null,
            collectionList = emptyList(),
            intToString = 0,
            innerMappingDomain = InnerMappingDto(0),
            listNlb = listOf(1.0f, 2.0f)
        )

    }
}
