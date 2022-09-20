package com.syouth.kmapper.testload.mappers

import com.syouth.kmapper.processor_annotations.Mapper
import com.syouth.kmapper.processor_annotations.Mapping
import com.syouth.kmapper.testload.domain.ExampleDomain
import com.syouth.kmapper.testload.dto.ExampleDto
import com.syouth.kmapper.testload.dto.TestDto

internal interface ExampleMapperSupport {
    fun mapIntToString(i: Int): String = i.toString()
    fun mapFloatToInt(f: Float): Int? = f.toInt()
}

@Mapper
internal interface ExampleMapper : ExampleMapperSupport {
    @Mapping(target = "float", source = "secondTestParam")
    @Mapping(target = "innerMappingDomain.nI", source = "testParam")
    @Mapping(target = "integer", source = "testDto.testI")
    @Mapping(target = "innerList.i", source = "testDto.testI")
    fun map(dto: ExampleDto, testParam: Int?, secondTestParam: Float, ii: Int, testDto: TestDto): ExampleDomain
    fun map(v: Double): Double = v / 2.5
}