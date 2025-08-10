package com.syouth.kmapper.data.models.listConvertionTest

import com.syouth.kmapper.data.models.Creator

internal data class OtherListDto(
    val k: String
) {
    companion object : Creator<OtherListDto> {
        override fun create() = OtherListDto(k = "constisdsdtuam")
    }
}

internal data class ListDto(
    val i: OtherListDto,
    val l: List<OtherListDto>
) {
    companion object : Creator<ListDto> {
        override fun create() =
            ListDto(i = OtherListDto.create(), l = listOf(OtherListDto.create()))
    }
}