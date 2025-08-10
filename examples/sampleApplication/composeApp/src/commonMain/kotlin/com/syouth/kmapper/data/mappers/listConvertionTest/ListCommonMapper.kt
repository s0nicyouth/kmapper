package com.syouth.kmapper.data.mappers.listConvertionTest

import com.syouth.kmapper.data.mappers.CommonMapper
import com.syouth.kmapper.data.models.listConvertionTest.ListDto
import com.syouth.kmapper.data.models.listConvertionTest.OtherListDto
import com.syouth.kmapper.domain.models.listConversionTest.ListDomain
import com.syouth.kmapper.processor_annotations.Bind
import com.syouth.kmapper.processor_annotations.Mapper

@Mapper
internal interface ListCommonMapper : CommonMapper<ListDto, ListDomain> {
    fun map(o: ListDto, @Bind l: List<OtherListDto>): ListDomain
}