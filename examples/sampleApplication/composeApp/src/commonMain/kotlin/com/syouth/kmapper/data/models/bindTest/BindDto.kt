package com.syouth.kmapper.data.models.bindTest

import com.syouth.kmapper.data.models.Creator

internal data class SomeDto(
    val i: Int
) {
    companion object : Creator<SomeDto> {

        override fun create(): SomeDto = SomeDto(0)

    }
}

internal data class BindDto(
    val one: String
) {
    companion object : Creator<BindDto> {

        override fun create(): BindDto = BindDto("one")

    }
}
