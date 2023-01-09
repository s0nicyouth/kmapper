package com.syouth.kmapper.testload.dto.listConvertionTest

internal data class OtherListDto(
    val k: String
)

internal data class ListDto(
    val i: OtherListDto,
    val l: List<OtherListDto>
)