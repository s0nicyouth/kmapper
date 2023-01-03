package com.syouth.kmapper.processor.injectors

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec

internal class KoinInjector(options: Map<String, String>): Injector {
    private val injectionType: ClassName = when (options["koinInjectionType"]?.lowercase()) {
        "single" -> singleAnnotationClassName
        else -> factoryAnnotationClassName
    }

    override val classModifier: KModifier = KModifier.INTERNAL

    override fun processClassSpec(builder: TypeSpec.Builder) {
        builder.addAnnotation(injectionType)
    }

    companion object{
        private val singleAnnotationClassName = ClassName("org.koin.core.annotation","Single")
        private val factoryAnnotationClassName = ClassName("org.koin.core.annotation","Factory")
    }
}