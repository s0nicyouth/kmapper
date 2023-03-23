package com.syouth.kmapper.testload.mappers.bindTest

import com.syouth.kmapper.processor_annotations.Bind
import com.syouth.kmapper.processor_annotations.Mapper
import com.syouth.kmapper.testload.domain.bindTest.BindDomain
import com.syouth.kmapper.testload.dto.bindTest.BindDto
import com.syouth.kmapper.testload.dto.bindTest.SomeInternalDto

@Mapper
internal interface BindMapper {
    fun map(dto: BindDto, @Bind second: Int, @Bind third: SomeInternalDto, @Bind(to = "fourth") pr: Double): BindDomain
}