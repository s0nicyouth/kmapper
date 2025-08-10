package com.syouth.kmapper.data.models.exhaustiveTest

import com.syouth.kmapper.data.models.Creator

internal data class InnerMappingDto(
    val i: Int
) {
    companion object : Creator<InnerMappingDto> {
        override fun create(): InnerMappingDto = InnerMappingDto(i = 5524)

    }
}
