package com.syouth.kmapper.testload.mappers.listConvertionTest

import com.syouth.kmapper.processor_annotations.Bind
import com.syouth.kmapper.processor_annotations.Mapper
import com.syouth.kmapper.testload.domain.listConversionTest.ListDomain
import com.syouth.kmapper.testload.dto.listConvertionTest.ListDto
import com.syouth.kmapper.testload.dto.listConvertionTest.OtherListDto

@Mapper
internal interface ListMapper {
    fun map(o: ListDto, @Bind l: List<OtherListDto>): ListDomain
}