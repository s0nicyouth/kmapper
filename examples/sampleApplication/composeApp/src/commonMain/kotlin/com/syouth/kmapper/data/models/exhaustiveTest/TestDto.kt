package com.syouth.kmapper.data.models.exhaustiveTest

import com.syouth.kmapper.data.models.Creator

internal data class TestDto(
    val testI: Int
) {
    companion object : Creator<TestDto> {
        override fun create() = TestDto(testI = 6202)

    }
}