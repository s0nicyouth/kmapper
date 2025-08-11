package com.syouth.kmapper.data.mappers.exhanustiveTest


import com.syouth.kmapper.data.mappers.CommonMapper
import com.syouth.kmapper.data.models.exhaustiveTest.ExampleDto
import com.syouth.kmapper.data.models.exhaustiveTest.TestDto
import com.syouth.kmapper.domain.models.exhaustiveTest.ExampleDomain
import com.syouth.kmapper.processor_annotations.Mapper
import com.syouth.kmapper.processor_annotations.Mapping

internal interface ExampleMapperSupport {
    fun mapIntToString(i: Int?): String? = i?.toString()
    fun mapFloatToInt(f: Float): Int? = f.toInt()
}

@Mapper
internal interface ExampleCommonMapper : ExampleMapperSupport,
    CommonMapper<ExampleDto, ExampleDomain> {
    @Mapping(target = "float", source = "secondTestParam")
    @Mapping(target = "innerMappingDomain.nI", source = "testParam")
    @Mapping(target = "integer", source = "testDto.testI")
    @Mapping(target = "innerList.i", source = "testDto.testI")
    fun map(
        dto: ExampleDto,
        testParam: Int?,
        secondTestParam: Float,
        ii: Int,
        testDto: TestDto
    ): ExampleDomain

    fun map(v: Double): Double = v / 2.5

    override fun map(input: ExampleDto): ExampleDomain {
        throw IllegalStateException("This dto - ${input::class.simpleName} " +
                    "does not supported now")
    }
}


