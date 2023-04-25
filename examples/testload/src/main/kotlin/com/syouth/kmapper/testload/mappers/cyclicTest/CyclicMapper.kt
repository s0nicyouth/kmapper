package com.syouth.kmapper.testload.mappers.cyclicTest

import com.syouth.kmapper.processor_annotations.Mapper
import com.syouth.kmapper.testload.domain.cyclicTest.FirstDataDomain
import com.syouth.kmapper.testload.dto.cyclicTest.FirstData

@Mapper
internal interface CyclicMapper {
    fun map(dto: FirstData): FirstDataDomain
}