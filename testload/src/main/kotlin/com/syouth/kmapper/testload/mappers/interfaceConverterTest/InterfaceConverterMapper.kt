package com.syouth.kmapper.testload.mappers.interfaceConverterTest

import com.syouth.kmapper.converters.BigDecimalTypeConverters
import com.syouth.kmapper.converters.PrimitiveTypeConverters
import com.syouth.kmapper.processor_annotations.Mapper
import com.syouth.kmapper.processor_annotations.Mapping
import com.syouth.kmapper.testload.domain.interfaceConvertersTest.InterfaceTestDomain
import com.syouth.kmapper.testload.dto.interfaceConvertersTest.InterfaceTestDto

@Mapper
interface InterfaceConverterMapper : PrimitiveTypeConverters, BigDecimalTypeConverters {
    @Mapping(target = "bigDecimalToStr", source = "from.bigDecimalToString")
    fun map(from: InterfaceTestDto): InterfaceTestDomain
}