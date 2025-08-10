package com.syouth.kmapper.data.models.exhaustiveTest

import com.syouth.kmapper.data.models.Creator

internal data class InnerDto(
    val i: Int,
    val nullableI: Int
) {
    companion object : Creator<InnerDto> {
        override fun create(): InnerDto {
            return InnerDto(i = 4512, nullableI = 3431)
        }
    }


}