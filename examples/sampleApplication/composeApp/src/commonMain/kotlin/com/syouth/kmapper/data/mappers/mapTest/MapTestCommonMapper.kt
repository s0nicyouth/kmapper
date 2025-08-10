package com.syouth.kmapper.data.mappers.mapTest

import com.syouth.kmapper.converters.BigDecimalTypeConverters
import com.syouth.kmapper.converters.PrimitiveTypeConverters
import com.syouth.kmapper.data.mappers.CommonMapper
import com.syouth.kmapper.data.models.mapTest.MapTestDto
import com.syouth.kmapper.domain.models.mapTest.MapTestDomain
import com.syouth.kmapper.processor_annotations.Mapper

@Mapper
internal interface MapTestCommonMapper : PrimitiveTypeConverters, BigDecimalTypeConverters,
    CommonMapper<MapTestDto, MapTestDomain>