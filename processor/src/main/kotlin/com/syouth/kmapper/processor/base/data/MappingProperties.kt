package com.syouth.kmapper.processor.base.data

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter

data class MappingProperties(
    val from: KSPropertyDeclaration?,
    val to: KSValueParameter
)
