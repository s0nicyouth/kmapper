package com.syouth.kmapper.processor.convertors

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.toTypeName
import com.syouth.kmapper.processor.base.PathHolder
import com.syouth.kmapper.processor.convertors.models.AssignableStatement

internal class UserDefinedMethodConverter(
    private val from: KSType,
    private val to: KSType,
    method: KSFunctionDeclaration
) : TypeConvertor {

    private val methodSpec = FunSpec
        .builder(method.simpleName.asString())
        .returns(to.toTypeName())
        .addParameter("from", from.toTypeName())
        .build()

    override fun isSupported(from: KSType?, to: KSType, targetPath: PathHolder?): Boolean {
        if (from == null) return false
        return from == this.from && to == this.to
    }

    override fun buildConversionStatement(
        fromParameterSpec: ParameterSpec?,
        from: KSType?,
        to: KSType,
        targetPath: PathHolder?
    ): AssignableStatement = AssignableStatement(
        code = buildCodeBlock {
            if (fromParameterSpec == null) throw IllegalStateException("From object name can't be null here")
            add("%N(%N)", methodSpec, fromParameterSpec)
        },
        requiresObjectToConvertFrom = true
    )
}