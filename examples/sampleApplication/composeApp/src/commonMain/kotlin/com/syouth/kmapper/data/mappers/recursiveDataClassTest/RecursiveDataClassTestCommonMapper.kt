package com.syouth.kmapper.data.mappers.recursiveDataClassTest

import com.syouth.kmapper.converters.BigDecimalTypeConverters
import com.syouth.kmapper.data.models.recursiveDataClassTest.RecursiveDataClassDto
import com.syouth.kmapper.domain.models.recursiveDataClassTest.RecursiveDataClassDomain
import com.syouth.kmapper.processor_annotations.Mapper
import com.syouth.kmapper.processor_annotations.Mapping

@Mapper
internal interface RecursiveDataClassTestCommonMapper : BigDecimalTypeConverters {
    @Mapping(
        target = "second.third.bigDecimalToFloat",
        source = "from.second.third.bigDecimalToFloatRenamed"
    )
    @Mapping(
        target = "second.third.stringToString",
        source = "from.second.third.stringToStringRenamed"
    )
    fun map(from: RecursiveDataClassDto): RecursiveDataClassDomain
}