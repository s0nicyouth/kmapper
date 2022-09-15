package com.syouth.kmapper.processor.base.data

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueParameter

data class MapperInformation(
    val mapperParams: List<KSValueParameter>,
    val to: KSType,
    val func: KSFunctionDeclaration
) {
    val from: KSValueParameter get() = mapperParams[0]
}
