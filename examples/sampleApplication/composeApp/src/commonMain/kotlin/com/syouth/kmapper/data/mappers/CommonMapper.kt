package com.syouth.kmapper.data.mappers

internal interface CommonMapper<in IN, out OUT> {
    fun map(input: IN): OUT
}