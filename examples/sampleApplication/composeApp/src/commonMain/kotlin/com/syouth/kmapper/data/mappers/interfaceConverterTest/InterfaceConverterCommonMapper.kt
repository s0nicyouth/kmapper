package com.syouth.kmapper.data.mappers.interfaceConverterTest


import com.syouth.kmapper.converters.BigDecimalTypeConverters
import com.syouth.kmapper.converters.PrimitiveTypeConverters
import com.syouth.kmapper.data.mappers.CommonMapper
import com.syouth.kmapper.data.models.interfaceConvertersTest.InterfaceTestDto
import com.syouth.kmapper.domain.models.interfaceConvertersTest.InterfaceTestDomain
import com.syouth.kmapper.processor_annotations.Mapper
import com.syouth.kmapper.processor_annotations.Mapping

@Mapper
internal interface InterfaceConverterCommonMapper : PrimitiveTypeConverters,
    BigDecimalTypeConverters, CommonMapper<InterfaceTestDto, InterfaceTestDomain> {
    @Mapping(target = "bigDecimalToStr", source = "from.bigDecimalToString")
    override fun map(from: InterfaceTestDto): InterfaceTestDomain
}