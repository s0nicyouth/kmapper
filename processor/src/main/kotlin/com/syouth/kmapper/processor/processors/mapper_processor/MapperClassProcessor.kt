package com.syouth.kmapper.processor.processors.mapper_processor

import com.google.devtools.ksp.symbol.KSClassDeclaration

internal interface MapperClassProcessor {
    fun process(type: KSClassDeclaration)
}