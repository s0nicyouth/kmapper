package com.syouth.kmapper.processor.injectors

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec

internal interface Injector {
    val classModifier: KModifier
    fun processClassSpec(builder: TypeSpec.Builder)
}