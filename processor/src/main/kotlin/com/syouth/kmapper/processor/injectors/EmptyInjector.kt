package com.syouth.kmapper.processor.injectors

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec

internal class EmptyInjector: Injector {
    override val classModifier: KModifier = KModifier.INTERNAL

    override fun processClassSpec(builder: TypeSpec.Builder) {
        // do nothing
    }
}