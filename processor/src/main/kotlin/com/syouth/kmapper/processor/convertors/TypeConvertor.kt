package com.syouth.kmapper.processor.convertors

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ParameterSpec
import com.syouth.kmapper.processor.base.Bundle
import com.syouth.kmapper.processor.base.PathHolder
import com.syouth.kmapper.processor.convertors.models.AssignableStatement

internal interface TypeConvertor {
    fun isSupported(from: KSType?, to: KSType, targetPath: PathHolder?): Boolean
    fun buildConversionStatement(
        fromParameterSpec: ParameterSpec?,
        from: KSType?,
        to: KSType, targetPath: PathHolder?,
        bundle: Bundle
    ): AssignableStatement
}