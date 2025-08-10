package com.syouth.kmapper.data.mappers.bindTest

import com.syouth.kmapper.data.mappers.CommonMapper
import com.syouth.kmapper.data.models.bindTest.BindDto
import com.syouth.kmapper.data.models.bindTest.SomeDto
import com.syouth.kmapper.domain.models.bindTest.BindDomain
import com.syouth.kmapper.processor_annotations.Bind
import com.syouth.kmapper.processor_annotations.Mapper

@Mapper
internal interface BindMapper : CommonMapper<BindDto, BindDomain> {
    fun map(
        dto: BindDto,
        @Bind second: Int,
        @Bind third: SomeDto,
        @Bind(to = "fourth") pr: Double
    ): BindDomain

    override fun map(input: BindDto): BindDomain {
        throw IllegalStateException("does not supported now")
    }
}

