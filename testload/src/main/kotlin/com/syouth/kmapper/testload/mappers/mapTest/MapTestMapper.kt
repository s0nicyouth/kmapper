package com.syouth.kmapper.testload.mappers.mapTest

import com.syouth.kmapper.converters.BigDecimalTypeConverters
import com.syouth.kmapper.converters.PrimitiveTypeConverters
import com.syouth.kmapper.processor_annotations.Mapper
import com.syouth.kmapper.testload.domain.mapTest.MapTestDomain
import com.syouth.kmapper.testload.dto.mapTest.MapTestDto

@Mapper
internal interface MapTestMapper : PrimitiveTypeConverters, BigDecimalTypeConverters{
    fun map(dto: MapTestDto): MapTestDomain
}