package com.syouth.kmapper.testload.mappers.recursiveDataClassTest

import com.syouth.kmapper.converters.BigDecimalTypeConverters
import com.syouth.kmapper.processor_annotations.Mapper
import com.syouth.kmapper.processor_annotations.Mapping
import com.syouth.kmapper.testload.domain.recursiveDataClassTest.RecursiveDataClassDomain
import com.syouth.kmapper.testload.dto.recursiveDataClassTest.RecursiveDataClassDto

@Mapper
internal interface RecursiveDataClassTestMapper : BigDecimalTypeConverters {
    @Mapping(target = "second.third.bigDecimalToFloat", source = "from.second.third.bigDecimalToFloatRenamed")
    @Mapping(target = "second.third.stringToString", source = "from.second.third.stringToStringRenamed")
    fun map(from: RecursiveDataClassDto): RecursiveDataClassDomain
}